import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity implements AnimatingContract
{
    private int animationPeriod;
    private int repeatCount;

    public AnimatedEntity(String id, Point position, List<PImage> images, int actionPeriod,
                          int repeatCount, int animationPeriod)
    {
        super(id, position, images, actionPeriod);
        this.repeatCount = repeatCount;
        this.animationPeriod = animationPeriod;
    }

    @Override
    public void nextImage()
    {
        int nextIndex = (super.getImageIndex() + 1) % super.getImages().size();
        super.setImageIndex(nextIndex);
    }

    @Override
    public int getAnimationPeriod()
    {
        return animationPeriod;
    }   // move

    @Override
    public void setAnimationPeriod(int animationPeriod)
    {
        this.animationPeriod = animationPeriod;
    }   // move

    public void scheduleActions(Entity e, EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, new Animation(this, world, imageStore, 0), getAnimationPeriod());
    }

    @Override
    public int getRepeatCount()
    {
        return repeatCount;
    }   // move

    @Override
    public void setRepeatCount(int repeatCount)
    {
        this.repeatCount = repeatCount;
    }   // move
}
