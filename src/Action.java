public abstract class Action implements ActionContract
{
    private Entity entity;
    private String actionKind;
    private WorldModel world;
    private ImageStore imageStore;

    public Action(Entity entity, WorldModel world, ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    @Override
    public abstract void executeAction(EventScheduler scheduler);

    @Override
    public WorldModel getWorld()
    {
        return world;
    }

    @Override
    public ImageStore getImageStore() {
        return imageStore;
    }

    @Override
    public String getActionKind() {
        return actionKind;
    }

    @Override
    public void setActionKind(String kind)
    {
        this.actionKind = kind;
    }

    public Entity getEntity()
    {
        return entity;
    }
}
