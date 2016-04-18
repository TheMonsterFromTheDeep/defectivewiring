package defectivewiring.sprite;

import alfredo.sprite.Bounds;
import alfredo.sprite.Drawable;
import alfredo.sprite.Force;

public class Pod extends Bounds implements Drawable {
    
    public static int FIRE_COOLDOWN = 30;
    
    public int cooldown = 0;
    
    public Pod() {
        super(16, 16); //16 x 16 bounding box cuz why not
    }
    
    @Override
    public float getDrawX() {
        return getWorldX() - 8;
    }

    @Override
    public float getDrawY() {
        return getWorldY();
    }

    @Override
    public float getDrawPivotX() {
        return getWorldX();
    }

    @Override
    public float getDrawPivotY() {
        return getWorldY();
    }

    @Override
    public double getDrawDirection() {
        return getWorldDirection();
    }
    
}