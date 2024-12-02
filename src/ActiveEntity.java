import processing.core.PImage;

import java.util.List;

public abstract class ActiveEntity extends Entity implements ActiveContract
{
    private int actionPeriod;

    public ActiveEntity(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    @Override
    public void setActionPeriod(int actionPeriod)
    {
        this.actionPeriod = actionPeriod;
    }

    @Override
    public int getActionPeriod() {
        return actionPeriod;
    }

    @Override
    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);    // must be abstract

    @Override
    public void scheduleActions(Entity e, EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        checkPortalDistance(world, imageStore, scheduler);
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), getActionPeriod());
    }// override if necessary
}
