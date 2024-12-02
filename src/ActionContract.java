/*
Action: ideally what our various entities might do in our virutal world
 */

public interface ActionContract
{
    void executeAction(EventScheduler scheduler);
    WorldModel getWorld();
    ImageStore getImageStore();
    String getActionKind();
    void setActionKind(String kind);
}
