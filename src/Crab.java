import processing.core.PImage;

//import java.rmi.activation.ActivationID;
import java.util.List;
import java.util.Optional;

public class Crab extends MovingEntity
{
    public Crab(String id, Point pos, List<PImage> imageList, int actionPeriod, int repeatCount, int animationPeriod)
    {
        super(id, pos, imageList, actionPeriod, repeatCount, animationPeriod);
        setKind("CRAB");
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) // this methods determines the activity of a crab
    {
        Optional<Entity> crabTarget = world.findNearest(getPosition(), "SGRASS");
        long nextPeriod = getActionPeriod();

        if (crabTarget.isPresent()) {
            Point tgtPos = crabTarget.get().getPosition();

            if (moveCreature(world, crabTarget.get(), scheduler, imageStore))
            {
                Quake quake = new Quake("quake", tgtPos, imageStore.getImageList("quake"), 0,0,0);  // values are set by the constructor in "Quake" class
                world.addEntity(quake);
                nextPeriod += getActionPeriod();
                quake.scheduleActions(quake, scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), nextPeriod);
    }

    public boolean moveCreature(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore)
    {
        checkPortalDistance(world, imageStore, scheduler);
        if (world.isAdjacent(getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
            {
            Point nextPos = nextPosition(world, target.getPosition(), this);

            if (!getPosition().equals(nextPos))
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
}


