import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Tank extends MovingEntity
{

    public Tank(String id, Point pos, List<PImage> images, int actionPeriod, int repeatCount, int animationPeriod)
    {
        super(id, pos, images, actionPeriod, repeatCount, animationPeriod);
        setKind("TANK");
    }

    @Override
    public boolean moveCreature(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore)  // moves like a crab, seeks a crab
    {
//        System.out.println("Ninja's trying to move");
        if (world.isAdjacent(getPosition(), target.getPosition()))
        {
//            world.removeEntity(this);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
//            world.removeEntity(this);
//            scheduler.unscheduleAllEvents(this);

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

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> tankTarget = world.findNearest(getPosition(), "CRAB");

        long nextPeriod = getActionPeriod();

        if (tankTarget.isPresent())
        {
            Point tgtPos = tankTarget.get().getPosition();

            if (moveCreature(world, tankTarget.get(), scheduler, imageStore))
            {
                Quake quake = new Quake("quake", tgtPos, imageStore.getImageList("quake"), 0,0,0);  // values are set by the constructor in "Quake" class
                world.addEntity(quake);
                nextPeriod += getActionPeriod();
                quake.scheduleActions(quake, scheduler, world, imageStore);
            }
        }

//        else
//        {
//            tankTarget = world.findNearest(getPosition(), "PORTAL");
//            Point tgtPos = tankTarget.get().getPosition();
//        }
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), nextPeriod);
    }

    @Override
    public void executePortalProximityEvent(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
     System.out.println("tanks laugh in the face of portals!");
    }
}
