import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Octo extends MovingEntity
{
    private int resourceLimit;
    private int resourceCount;

    public Octo(String id , Point position, List<PImage> images, int actionPeriod, int repeatCount,
                int animationPeriod, int resourceLimit, int resourceCount)
    {
        super(id, position, images, actionPeriod, repeatCount, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    @Override
    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    @Override
    public void executePortalProximityEvent(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        DiseasedOcto diseasedOcto = new DiseasedOcto(getId(), getPosition(), imageStore.getImageList("diseased"), getActionPeriod(), getAnimationPeriod(),
                getRepeatCount(), 0, 0);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        world.addEntity(diseasedOcto);
        diseasedOcto.scheduleActions(diseasedOcto, scheduler, world, imageStore);
    }

    public int getResourceLimit()
    {
        return resourceLimit;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount)
    {
        this.resourceCount = resourceCount;
    }
}
