import processing.core.PImage;

import java.util.List;

public interface ActiveContract
{
    int getActionPeriod();
    void setActionPeriod(int actionPeriod);
    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    void scheduleActions(Entity e, EventScheduler scheduler, WorldModel world, ImageStore imageStore);
}
