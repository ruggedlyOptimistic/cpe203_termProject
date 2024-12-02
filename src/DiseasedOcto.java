import processing.core.PImage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class DiseasedOcto extends OctoFull
{

    public DiseasedOcto(String id, Point position, List<PImage> images, int actionPeriod, int repeatCount, int animationPeriod, int resourceLimit, int resourceCount)
    {
        super(id, position, images, actionPeriod, repeatCount, animationPeriod, resourceLimit, resourceCount);
        setKind("DISEASED_OCTO");
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> target = world.findNearest(getPosition(), "ATLANTIS");
        try
        {
            Atlantis atlantis = (Atlantis) target.get();
            if (target.isPresent() && moveCreature(world, atlantis, scheduler, imageStore))
            {
                //at atlantis trigger animation

                transformAtlantis(world, scheduler, imageStore, atlantis);
            }
            else
            {  // scheduleEvent(entity, action, long)
                Activity activity = new Activity(this, world, imageStore);
                scheduler.scheduleEvent(this, activity, this.getActionPeriod());
            }
        }
        catch (NoSuchElementException e)
        {
            Activity activity = new Activity(this, world, imageStore);
            scheduler.scheduleEvent(this, activity, this.getActionPeriod());
        }
    }

    @Override
    public boolean moveCreature(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore)
    {
        if (world.isAdjacent(this.getPosition(), target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition(), target);

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }
                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public void transformAtlantis(WorldModel world, EventScheduler scheduler, ImageStore imageStore, Atlantis atlantis)
    {
        SGrass sGrass = new SGrass("sgrass", this.getPosition(), imageStore.getImageList("seaGrass"), 0);

        world.removeEntity(this);
        world.removeEntity(atlantis);
        scheduler.unscheduleAllEvents(this);
        scheduler.unscheduleAllEvents(atlantis);
        world.addEntity(sGrass);
        sGrass.scheduleActions(sGrass, scheduler, world, imageStore);
    }

    public void executePortalProximityEvent(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        System.out.println("Already a diseased octo!");
    }
}
