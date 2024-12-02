import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity
{
    public Obstacle(String id, Point pos, List<PImage> imageList)
    {
        super(id, pos, imageList);
        super.setKind("OBSTACLE");
    }
}
