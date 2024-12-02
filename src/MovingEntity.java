import processing.core.PImage;

import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javax.swing.UIManager.get;

public abstract class MovingEntity extends AnimatedEntity implements MovingContract
{
    private PathingStrategy navigationMode; // move the options list to virtual world

    public MovingEntity(String id, Point position, List<PImage> images, int actionPeriod, int repeatCount, int animationPeriod)
    {
        super(id, position, images, actionPeriod, repeatCount, animationPeriod);

//        navigationMode = new AStarPathingStrategy();
        navigationMode = new SingleStepPathingStrategy();
    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos, Entity target)
    {
       Point current = this.getPosition();
       Predicate<Point> canPassThrough = n -> !world.isOccupied(n) && world.withinBounds(n);
       BiPredicate<Point, Point> withinReach = (a, b) -> {return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2)) == 1;};

        List<Point> path = navigationMode.computePath(current, destPos, canPassThrough, withinReach, PathingStrategy.CARDINAL_NEIGHBORS);

//        System.out.println(this.getKind() + " is trying to move to " + path.get(0) + " to find target " + target.getKind());
       return path.get(0);  // index 0 is the next position, index last is the target position
    }

    @Override
    public abstract boolean moveCreature(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore);

    @Override
    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);    // This must be abstract: its actions depend on the type of entity is calling it
}
