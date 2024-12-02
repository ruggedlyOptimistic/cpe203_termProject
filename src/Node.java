import java.util.List;

public class Node
{
    private Point location;
    private boolean visited;
    private Node parent;
    private List<Node> neighbors;
    private double cost;

    public Node(Point location, boolean visited, Node parent, double cost)
    {
        this.location = location;
        this.visited = visited;
        this.parent = parent;
        this.cost = cost;
    }

    public Point getLocation()
    {
        return location;
    }

    public Node getParent()
    {
        return parent;
    }

    public List<Node> getNeighbors()
    {
        return neighbors;
    }

    public double getCost()
    {
        return cost;
    }

    public void setCost(double c)
    {
        this.cost = c;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public void setVisited(boolean flag)
    {
        this.visited = flag;
    }

    @Override
    public String toString()
    {
        return "Node @ location : (" + location.getX() + ", " + location.getY() + ")";
    }
}