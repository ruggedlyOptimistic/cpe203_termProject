public class Activity extends Action
{
    private ActiveEntity activeEntity;

        public Activity(Entity entity, WorldModel world, ImageStore imageStore)
        {
            super(entity, world, imageStore);
            this.activeEntity = (ActiveEntity) entity;
            setActionKind("ACTIVITY");
        }

    @Override
        public void executeAction(EventScheduler scheduler)
        {
            getActiveEntity().executeActivity(getWorld(), getImageStore(), scheduler);
        }

        public ActiveEntity getActiveEntity()
        {
            return activeEntity;
        }
}




