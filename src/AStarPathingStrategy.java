import org.w3c.dom.ls.LSOutput;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy extends PathingAlgorithm
{
    // write a node class keeping track of its Point location, its Parent Node, and its cost to get there

    @Override
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors)
    {
        /*
1  procedure BFS(G,start_v):
2      let Q be a queue
3      label start_v as discovered
4      Q.enqueue(start_v)
5      while Q is not empty
6          v = Q.dequeue()
7          if v is the goal:
8              return v
9          for all edges from v to w in G.adjacentEdges(v) do
10             if w is not labeled as discovered:
11                 label w as discovered
12                 w.parent = v
13                 Q.enqueue(w)
*/

        List<Node> closed = new ArrayList<>();
        ArrayList<Point> path = new ArrayList<>();
        PriorityQueue<Node> q = new PriorityQueue<>(500, new Comparator<Node>(){
            public int compare(Node a, Node b)
            {
                if (a.getCost() > b.getCost())
                {
                    return 1;
                }
                else if (a.getCost() < b.getCost())
                {
                    return -1;
                }
                else {return 0;}
            }
        });

        Node n = new Node(start, true, null, pathDistance(start, end)); // my starting node
        Point current = n.getLocation();
        path.add(current);  // adding the starting location of the first node to the queue
        Node finalN = n;
        List<Node> nearby = potentialNeighbors.apply(current)
                .filter(canPassThrough)
                .map(t -> new Node(t, false, finalN, computeF(start, t, end)))
                .sorted(Comparator.comparing(Node::getCost).reversed())
                .collect(Collectors.toList());

        Node R = nearby.get(0);
        double r = R.getCost();

        for (Node sample : nearby)
        {
            if (r > sample.getCost() && !sample.isVisited())
            {
                R = sample;
                r = sample.getCost();
            }
        }
//            computeF(nearby, start, end); // sets the cost of visiting that neighbor
        q.add(R);
//        q.addAll(nearby); // adding its neighbors to the queue
//        System.exit(0);

        while (!withinReach.test(current, end) && !q.isEmpty())
        {
            System.out.println(n);
            n = q.poll();  // should grab the closest neighbor off the top of the queue
            current = n.getLocation();
            n.setVisited(true); // marking this as a visited node
            path.add(current);
            Node currentNode = n;
            nearby = potentialNeighbors.apply(current)
                    .filter(canPassThrough)
                    .map(t -> new Node(t, false, currentNode, computeF(start, t, end)))
                    .sorted(Comparator.comparing(Node::getCost).reversed())
                    .collect(Collectors.toList());

            Node T = nearby.get(0);
            double t = T.getCost();

            for (Node sample : nearby)
            {
                if (t > sample.getCost() && !sample.isVisited())
                {
                    T = sample;
                    t = sample.getCost();
                }
            }
//            computeF(nearby, start, end); // sets the cost of visiting that neighbor
            q.add(T);
        }
        System.out.println("SNIPE Exited the loop");
        return path;
    }

    private double computeF(Point start, Point current, Point end)
    {

        double g = pathDistance(start, current);   // plus 1 to include distance to nearest neighbor
        double h = pathDistance(current, end);    // heuristic function
        return (g + h);

    }
    private Double pathDistance(Point a, Point b)
    {
        return Math.sqrt(Math.pow(b.getY() - a.getY(), 2) +
                Math.pow(b.getX() - a.getX(), 2));
    }
}
