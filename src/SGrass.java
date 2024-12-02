import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SGrass extends ActiveEntity
{
    public SGrass(String id, Point pos, List<PImage> imageList, int actionPeriod)
    {
        super(id, pos, imageList, actionPeriod);
        super.setKind("SGRASS");
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)  // this methods determines the activity of seagrass
    {
        checkPortalDistance(world, imageStore, scheduler);

       Random RNG = new Random();
       final String FISH_ID_PREFIX = "fish -- ";
       final int FISH_CORRUPT_MIN = 20000;
       final int FISH_CORRUPT_MAX = 30000;


        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent()) // if the space around the grass is open, put a fish here and assign a randomized "corrupt" period
        {
            Fish fish = new Fish(FISH_ID_PREFIX + getId(), openPt.get(), imageStore.getImageList("fish"),
                    FISH_CORRUPT_MIN + RNG.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN));
            world.addEntity(fish);
            fish.scheduleActions(fish, scheduler, world, imageStore); // previously was passing "this" into scheduleActions rather than "fish"

        }
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), getActionPeriod());
    }
}
