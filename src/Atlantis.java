import processing.core.PImage;

import java.util.List;

public class Atlantis extends AnimatedEntity
{
    public Atlantis(String id, Point position, List<PImage> images,int actionPeriod, int animationPeriod, int repeatCount)
    {
        super(id, position, images, actionPeriod, animationPeriod, repeatCount);
        super.setKind("ATLANTIS");
        super.setRepeatCount(7);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
    }

    public void scheduleActions(Entity e, EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, new Animation(this, null, null, getRepeatCount()), getAnimationPeriod());
    }
}
