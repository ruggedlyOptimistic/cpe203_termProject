import processing.core.PImage;

import java.util.List;

public interface AnimatingContract
{
    int getAnimationPeriod();
    void setAnimationPeriod(int animationPeriod);
    int getImageIndex();
    void nextImage();
    int getRepeatCount();
    void setRepeatCount(int repeatCount);
}
