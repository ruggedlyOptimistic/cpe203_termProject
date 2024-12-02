import processing.core.PImage;

import javax.swing.Action;
import java.util.List;
import java.util.Random;

public class Fish extends ActiveEntity
{
    public Fish(String id, Point pos, List<PImage> imageList, int actionPeriod)
    {
        super(id, pos, imageList, actionPeriod);
        super.setKind("FISH");
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) // this method creates a crab in place of a fish
    {
        final String CRAB_KEY = "crab";
        final String CRAB_ID_SUFFIX = " -- crab";
        final int CRAB_PERIOD_SCALE = 4;
        final int CRAB_ANIMATION_MIN = 50;
        final int CRAB_ANIMATION_MAX = 150;

        Random RNG = new Random();

        Point pos = getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Crab crab = new Crab(getId() + CRAB_ID_SUFFIX, pos, imageStore.getImageList(CRAB_KEY),getActionPeriod() / CRAB_PERIOD_SCALE,0, CRAB_ANIMATION_MIN +
                RNG.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN));
        world.addEntity(crab);
        crab.scheduleActions(crab, scheduler, world, imageStore);
    }
}
