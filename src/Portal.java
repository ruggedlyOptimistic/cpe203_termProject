import processing.core.PImage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Portal extends AnimatedEntity
{
    private ArrayList<Point> reach = new ArrayList<>();
    private String[] targets;

    public Portal(String id, Point position, List<PImage> images, int actionPeriod, int repeatCount, int animationPeriod)
    {
        super(id, position, images, actionPeriod, repeatCount, animationPeriod);
        setKind("PORTAL");

        reach = setReach(position);
        targets = new String[]{"CRAB", "OBSTACLE", "FISH", "SGRASS"};

    }

    private ArrayList<Point> setReach(Point position)
    {
        for (int x = -4; x <= 4; x++)
        {
            for (int y = -4; y <= 4; y++)
            {
                reach.add(new Point(position.getX() + x, position.getY() + y));
            }
        }
        return reach;
    }

    public ArrayList<Point> getReach()
    {
        return reach;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
    }

    public void scheduleActions(Entity e, EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, new Animation(this, null, null, getRepeatCount()), getAnimationPeriod());
        scanReachZone(world, imageStore, scheduler);
    }

    public void scanReachZone(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        for (int index = 0; index < reach.size(); index ++) // destroys nearby entities
        {
            if (world.isOccupied(reach.get(index)))
            {
//                System.out.println("Portal reach is occupied at position: (" + reach.get(index).getX() + ", " + reach.get(index).getY() + ")");
                Entity occupant = ((Entity) world.getOccupant(reach.get(index)).get());
                if (occupant.getKind().equals("OCTO_FULL") || occupant.getKind().equals("OCTO_NOT_FULL"))
                {
                    DiseasedOcto diseasedOcto = new DiseasedOcto(getId(), getPosition(), imageStore.getImageList("diseased"), getActionPeriod(), getAnimationPeriod(),
                            getRepeatCount(), 0, 0);

                    world.removeEntity(occupant);
                    scheduler.unscheduleAllEvents(occupant);
                    world.addEntity(diseasedOcto);
                    diseasedOcto.scheduleActions(diseasedOcto, scheduler, world, imageStore);
                }
                else
                {
                    for (int i = 0; i < targets.length; i++)
                    {
                        if (occupant.getKind().equals(targets[i]))
                        {
                            world.removeEntity(occupant);
                            scheduler.unscheduleAllEvents(occupant);

                            Quake quake = new Quake("quake", reach.get(index), imageStore.getImageList("quake"),
                                    0,0,0);
                            world.tryAddEntity(quake);
                            quake.scheduleActions(quake, scheduler, world, imageStore);
                        }
                        occupant = ((Entity) world.getOccupant(reach.get(index)).get());
                    }
                }

            }
        }
    }

    }

