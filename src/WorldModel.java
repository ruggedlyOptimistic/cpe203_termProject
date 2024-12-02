import processing.core.PImage;

import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{
   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;

   private final int FISH_REACH = 1;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

    public int getNumRows()
    {
        return numRows;
    }
    public int getNumCols()
    {
        return numCols;
    }
    public Background[][] getBackground() {
        return background;
    }
    public Set<Entity> getEntities() {
        return entities;
    }
    public Entity[][] getOccupancy() {
        return occupancy;
    }

    public boolean contains(String kind)
    {
        boolean flag = false;

        for (Entity e : entities)
        {
            if (e.getKind().equals(kind))
            {
                flag = true;
                break;
            }
        }

        return flag;
    }

    public Optional<Point> findOpenPosition(Point pos)  // finds maximum legal distance from the cursor that you can put something
    {
        int limit = 0;
        Point test = new Point(pos.getX()+limit, pos.getY() + limit);
        while (withinBounds(test) && limit < 3)
        {
            limit++;
            test = new Point(pos.getX()+limit, pos.getY() + limit);
        }

        if (limit >=3)
        {
            limit -=1;
        }


        for (int dy = 0; dy <= limit; dy++)
        {
            for (int dx = 0; dx <= limit; dx++)
            {
                Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
                if (withinBounds(newPt) && !isOccupied(newPt))
                {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    public Optional<Point> findOpenAround(Point pos)
    {
        for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++)
        {
            for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++)
            {
                Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
                if (withinBounds(newPt) &&
                        !isOccupied(newPt))
                {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    public void tryAddEntity(Entity entity)
    {
        if (isOccupied(entity.getPosition()))
        {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }

    public boolean withinBounds(Point pos)
    {
//        System.out.println("Point: " + pos);
        return pos.getY() >= 0 && pos.getY() < getNumRows() &&
                pos.getX() >= 0 && pos.getX() < getNumCols();
    }

    public boolean isOccupied(Point pos)
    {
        return withinBounds(pos) && getOccupancyCell(pos) != null;
    }

    public boolean isAdjacent(Point p1, Point p2) // move to worldModel, rename "isAdjacent" using same parameters
    {
        return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) ||
                (p1.getY() == p2.getY() && Math.abs(p1.getX() - p2.getX()) == 1);
    }

    public Optional<Entity> findNearest(Point pos, String kind)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : getEntities())
        {
            if (entity.getKind() == kind)
            {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

    public Optional<Entity> nearestEntity(List<Entity> entities, Point pos)
    {
        if (entities.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities)
            {
                int otherDistance = distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance)
                {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public int distanceSquared(Point p1, Point p2)
    {
        int deltaX = p1.getX() - p2.getX();
        int deltaY = p1.getY() - p2.getY();

        return deltaX * deltaX + deltaY * deltaY;
    }

    /*
       Assumes that there is no entity currently occupying the
       intended destination cell.
    */
    public void addEntity(Entity entity)
    {
        if (withinBounds(entity.getPosition()))
        {
            setOccupancyCell(entity.getPosition(), entity);
            getEntities().add(entity);
        }
    }

    public void moveEntity(Entity entity, Point pos)
    {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos))
        {
            setOccupancyCell(oldPos, null); // classifies what is occupying a position
            removeEntityAt(pos);
            setOccupancyCell(pos, entity);  //puts the entity in that position
            entity.setPosition(pos);    // updates the entity's position
        }
    }

    public void removeEntity(Entity entity)
    {
        removeEntityAt(entity.getPosition());
    }

    public void removeEntityAt(Point pos)
    {
        if (withinBounds(pos)
                && getOccupancyCell(pos) != null)
        {
            Entity entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
            entity.setPosition(new Point(-1, -1));
            getEntities().remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos)
    {
        if (withinBounds(pos))
        {
            return Optional.of(getCurrentImage(getBackgroundCell(pos)));
        }
        else
        {
            return Optional.empty();
        }
    }

    public void setBackground(Point pos, Background background)
    {
        if (withinBounds(pos))
        {
            setBackgroundCell(pos, background);
        }
    }

    public Optional<Entity> getOccupant(Point pos)
    {
        if (isOccupied(pos))
        {
            return Optional.of(getOccupancyCell(pos));
        }
        else
        {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell(Point pos)
    {
        return getOccupancy()[pos.getY()][pos.getX()];
    }

    public void setOccupancyCell(Point pos, Entity entity)
    {
        getOccupancy()[pos.getY()][pos.getX()] = entity;
    }

    public Background getBackgroundCell(Point pos)
    {
        return getBackground()[pos.getY()][pos.getX()];
    }

    public void setBackgroundCell(Point pos, Background background)
    {
        getBackground()[pos.getY()][pos.getX()] = background;
    }
    public PImage getCurrentImage(Object entity)
    {
        if (entity instanceof Background)
        {
            return ((Background)entity).getImages()
                    .get(((Background)entity).getImageIndex());
        }
        else if (entity instanceof Entity)
        {
            return ((Entity)entity).getImages().get(((Entity)entity).getImageIndex());
        }
        else
        {
            throw new UnsupportedOperationException(
                    String.format("getCurrentImage not supported for %s",
                            entity));
        }
    }
}
