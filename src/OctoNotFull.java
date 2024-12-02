import processing.core.PImage;

//import java.rmi.activation.ActivationID;
import java.util.List;
import java.util.Optional;

public class OctoNotFull extends Octo
{
    public OctoNotFull(String id, Point pos, List<PImage> images, int actionPeriod, int repeatCount, int animationPeriod, int resourceLimit, int resourceCount)
    {
        super(id, pos, images, actionPeriod, animationPeriod, repeatCount, resourceLimit, resourceCount);
        setKind("OCTO_NOT_FULL");
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)   // this methods determines the activity of a not full octopus
     {
        Optional<Entity> notFullTarget = world.findNearest(getPosition(), "FISH");

        if (!notFullTarget.isPresent() || !moveCreature(world, notFullTarget.get(), scheduler, imageStore) || !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), getActionPeriod());
        }
    }

    @Override
    public boolean moveCreature(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore) // must override
    {
        checkPortalDistance(world, imageStore, scheduler);
        if (world.isAdjacent(this.getPosition(), target.getPosition()))
        {
            updateResourceCount(1);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = nextPosition(world, target.getPosition(), target);

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) // transforms a notFull octo into a full octo
    {
        if (this.getResourceCount() >= this.getResourceLimit())
        {
            OctoFull octoFull = new OctoFull(getId(),this.getPosition(), this.getImages(), getActionPeriod(),0, getAnimationPeriod(), getResourceLimit(), getResourceLimit());
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            world.addEntity(octoFull);
            octoFull.scheduleActions(octoFull, scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void updateResourceCount(int n)
    {
        this.setResourceCount(getResourceCount() + n);
    } // call this method each time the octo reaches atlantis to increment resource count by one
}