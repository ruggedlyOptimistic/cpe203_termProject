import processing.core.PImage;

import java.util.List;

public class Quake extends AnimatedEntity
{
    private final String QUAKE_KEY = "quake";
    private final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    private final String QUAKE_ID = "quake";
    private final int QUAKE_ACTION_PERIOD = 1100;
    private final int QUAKE_ANIMATION_PERIOD = 100;

    public Quake(String id, Point pos, List<PImage> imageList, int actionPeriod, int repeatCount, int animationPeriod)
    {
        super(id, pos, imageList, actionPeriod, repeatCount, animationPeriod);
        super.setKind("QUAKE");
        super.setActionPeriod(QUAKE_ACTION_PERIOD);
        super.setAnimationPeriod(QUAKE_ANIMATION_PERIOD);
        super.setRepeatCount(QUAKE_ANIMATION_REPEAT_COUNT);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)   // this methods determines what happens in the event of an earthquake
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
}