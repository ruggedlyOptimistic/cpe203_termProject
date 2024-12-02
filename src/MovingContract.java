import java.awt.*;

public interface MovingContract
{
  boolean moveCreature(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore);
  Point nextPosition(WorldModel world, Point destPos, Entity target);
}
