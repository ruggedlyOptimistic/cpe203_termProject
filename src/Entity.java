import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Entity implements EntityContract
{
    private String kind;
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(String id, Point position, List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
    }

    @Override
    public String getKind()
    {
        return kind;
    }

    @Override
    public void setKind(String kind)
    {
        this.kind = kind;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point pos)
    {
        this.position = pos;
    }

    @Override
    public List<PImage> getImages()
    {
        return images;
    }

    @Override
    public int getImageIndex()
    {
        return imageIndex;
    }

    @Override
    public void setImageIndex(int nextIndex)
    {
        this.imageIndex = nextIndex;
    }

    public void checkPortalDistance(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        if (world.contains("PORTAL"))
        {
            Optional<Entity> nearestPortal = world.findNearest(getPosition(), "PORTAL");
            int distance = (int) Math.sqrt(world.distanceSquared(nearestPortal.get().getPosition(), this.getPosition()));

            if (distance <= 5)
            {
                executePortalProximityEvent(world, imageStore, scheduler);
            }
        }
    }

    public void executePortalProximityEvent(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Quake quake = new Quake("quake", this.getPosition(), imageStore.getImageList("quake"),
                0,0,0);
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        world.tryAddEntity(quake);
        quake.scheduleActions(quake, scheduler, world, imageStore);
    }
}
