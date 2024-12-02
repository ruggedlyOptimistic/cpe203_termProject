import processing.core.PImage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class OctoFull extends Octo
{
    public OctoFull(String id, Point position, List<PImage> images, int actionPeriod, int repeatCount, int animationPeriod,
                    int resourceLimit, int resourceCount)
    {
        super(id, position, images, actionPeriod, animationPeriod, repeatCount, resourceLimit, resourceCount);
        setKind("OCTO_FULL");
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
                atlantis.scheduleActions(target.get(), scheduler, world, imageStore); // Atlantis parties!!!
                //transform to unfull
                transformFull(world, scheduler, imageStore);
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
        checkPortalDistance(world, imageStore , scheduler);
        if (world.isAdjacent(this.getPosition(), target.getPosition()))
        {
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

    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) // transforms a full octo into a notFull octo
    {
        OctoNotFull octoNotFull = new OctoNotFull(getId(), getPosition(), getImages(), getActionPeriod(), getAnimationPeriod(),
                getRepeatCount(), getResourceLimit(), 0);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        world.addEntity(octoNotFull);
        octoNotFull.scheduleActions(octoNotFull, scheduler, world, imageStore);
    }
}
