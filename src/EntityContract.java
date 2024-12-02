import java.util.List;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */
// reference previous conditions in "EntityLog.txt"

public interface EntityContract
{
   String getKind();
   void setKind(String kind);
   String getId();
   Point getPosition();
   void setPosition(Point pos);
   List<PImage> getImages();
   int getImageIndex();
   void setImageIndex(int nextIndex);
   void checkPortalDistance(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
   void executePortalProximityEvent(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
