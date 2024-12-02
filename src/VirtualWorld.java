import processing.core.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld extends PApplet
{
   private static final int VIEW_WIDTH = 640;
   private static final int VIEW_HEIGHT = 480;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private final String IMAGE_LIST_FILE_NAME = "imagelist";
   private final String DEFAULT_IMAGE_NAME = "background_default";
   private final int DEFAULT_IMAGE_COLOR = 0x808080;
   private final String LOAD_FILE_NAME = "gaia.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;
   private static double timeScale = 1.0;
   private final int TIMER_ACTION_PERIOD = 100;

   private static final int KEYED_IMAGE_MIN = 5;
   private static final int KEYED_RED_IDX = 2;
   private static final int KEYED_GREEN_IDX = 3;
   private static final int KEYED_BLUE_IDX = 4;

   private static final int PROPERTY_KEY = 0;

   private long next_time;
   private WorldModel world;
   private ImageStore imageStore;
   private WorldView view;
   private EventScheduler scheduler;
   private Jukebox jukebox;

   private PathingStrategy navMode;

   private int counter = 1;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup() // Creates the imagestore, worldModel, worldView, and the eventScheduler
{
      this.imageStore = new ImageStore(createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS, new Background(DEFAULT_IMAGE_NAME, imageStore.getImageList(DEFAULT_IMAGE_NAME)));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);
      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;

      this.jukebox = new Jukebox("underTheSea.mp3");
//      jukebox.playSong();

   }

   public void mousePressed()
   {
      counter++;
//      System.out.println("Mouse Location: (" + mouseX + ", " + mouseY + ")");

      if (counter == 2)
      {
         try
         {
            jukebox.stopSong();
            jukebox.setSong("Shoot_to_Thrill.mp3");
//            jukebox.playSong();
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }

      int cursorX = (mouseX / 32) + view.getViewport().getCol();
      int cursorY = (mouseY / 32) + view.getViewport().getRow();

//      System.out.println("Cursor: (" + cursorX + ", " + cursorY + ")");
//      System.out.println("Mouse Info: " + MouseInfo.getPointerInfo().getLocation().x + ", " + MouseInfo.getPointerInfo().getLocation().y);

      Point cursor = new Point(cursorX, cursorY);
      Optional<Point> unoccupied = world.findOpenPosition(cursor);  // finding an unoccupied position around where the mouse was clicked
      Portal portal = new Portal("PORTAL" , new Point(unoccupied.get().getX(), unoccupied.get().getY()), imageStore.getImageList("portal"), 0, 0, 0);

      world.tryAddEntity(portal);
      portal.scheduleActions(portal, scheduler, world, imageStore);

      for (int x = -4; x <= 4; x++)
      {
         for (int y = -4; y <= 4; y++)
         {
            Point p1 = new Point(cursorX + x, cursorY +y);
            Point p2 = new Point(cursorX -x, cursorY -y);
            if (world.withinBounds(p1))
            {
               world.setBackground(p1, new Background("background", imageStore.getImageList("space")));
            }
            if (world.withinBounds(p2))
            {
               world.setBackground(p2, new Background("background", imageStore.getImageList("space")));
            }
         }
      }

      unoccupied = world.findOpenPosition(cursor);  // finding an unoccupied position around where the mouse was clicked
      Tank tank = new Tank("TANK", new Point(unoccupied.get().getX(), unoccupied.get().getY()), imageStore.getImageList("tank"),
              0, 0, 0);
      world.tryAddEntity(tank);
      tank.scheduleActions(tank, scheduler, world, imageStore);
   }

   public static void setAlpha(PImage img, int maskColor, int alpha)
   {
      final int COLOR_MASK = 0xffffff;

      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha)
         {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         view.shiftView(dx, dy);
      }

   }

   public boolean parseBackground(String [] properties, WorldModel world, ImageStore imageStore)
   {
      final int BGND_NUM_PROPERTIES = 4;
      final int BGND_ID = 1;
      final int BGND_COL = 2;
      final int BGND_ROW = 3;

      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]), Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         world.setBackground(pt, new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   public PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private void loadImages(String filename, ImageStore imageStore, PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         loadImages(in, imageStore, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public void loadWorld(WorldModel world, String filename, ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         load(in, world, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public void load(Scanner in, WorldModel world, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), world, imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   } // called by VirtualWorld.loadWorld()

   public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
   {
      ArrayList<ActiveEntity> activeEntities = new ArrayList<>();
      for (Entity entity : world.getEntities())
      {
         String kind = entity.getKind();

         if (kind.equals("CRAB") || kind.equals("OCTO_FULL") || kind.equals("OCTO_NOT_FULL") || kind.equals("FISH") || kind.equals("SGRASS"))
         {
            activeEntities.add((ActiveEntity) entity);
         }
      }

      for (ActiveEntity entity : activeEntities)
      {
         //Only start actions for entities that include action (not those with just animations)
         if (entity.getActionPeriod() > 0)
            entity.scheduleActions(entity, scheduler, world, imageStore);
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static List<PImage> getImages(Map<String, List<PImage>> images, String key)
   {
      List<PImage> imgs = images.get(key);
      if (imgs == null)
      {
         imgs = new LinkedList<>();
         images.put(key, imgs);
      }
      return imgs;
   }

   public static void processImageLine(Map<String, List<PImage>> images, String line, PApplet screen)
   {
      String[] attrs = line.split("\\s");
      if (attrs.length >= 2)
      {
         String key = attrs[0];
         PImage img = screen.loadImage(attrs[1]);
         if (img != null && img.width != -1)
         {
            List<PImage> imgs = getImages(images, key);
            imgs.add(img);

            if (attrs.length >= KEYED_IMAGE_MIN)
            {
               int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
               int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
               int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
               setAlpha(img, screen.color(r, g, b), 0);
            }
         }
      }
   }

   public static void loadImages(Scanner in, ImageStore imageStore, PApplet screen)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            processImageLine(imageStore.getImages(), in.nextLine(), screen);
         }
         catch (NumberFormatException e)
         {
            System.out.println(String.format("Image format error on line %d",
                    lineNumber));
         }
         lineNumber++;
      }
   }

   public boolean processLine(String line, WorldModel world, ImageStore imageStore) // loads the entities
   {
      EntityParser parser = new EntityParser();

      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case "background":
               return parseBackground(properties, world, imageStore);
            case "octo":
               return parser.parseOctoNotFull(properties, world, imageStore);
            case "obstacle":
               return parser.parseObstacle(properties, world, imageStore);
            case "fish":
               return parser.parseFish(properties, world, imageStore);
            case "atlantis":
               return parser.parseAtlantis(properties, world, imageStore);
            case "seaGrass":
               return parser.parseSgrass(properties, world, imageStore);
         }
      }
      return false;
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
