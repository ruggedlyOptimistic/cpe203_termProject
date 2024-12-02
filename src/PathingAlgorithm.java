import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class PathingAlgorithm implements PathingStrategy
{
    public abstract List<Point> computePath(Point start, Point end,
                                            Predicate<Point> canPassThrough,
                                            BiPredicate<Point, Point> withinReach,
                                            Function<Point, Stream<Point>> potentialNeighbors);
}
