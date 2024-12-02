public class Animation extends Action
{
    private AnimatedEntity animatedEntity;
    private int repeatCount;

    public Animation(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount)
    {
        super(entity, world, imageStore);
        this.repeatCount = repeatCount;
        this.animatedEntity = (AnimatedEntity) entity;
        setActionKind("ANIMATION");
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    @Override
    public void executeAction(EventScheduler scheduler) // getter used instead of this.~~~ syntax
    {
            AnimatedEntity animatingType = (AnimatedEntity) super.getEntity();
            animatingType.nextImage();

            if (getRepeatCount() != 1)
            {
                scheduler.scheduleEvent(animatingType, new Animation(animatingType, getWorld(), getImageStore(), Math.max(getRepeatCount() - 1, 0)), animatingType.getAnimationPeriod());
            }
    }
}