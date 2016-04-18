package defectivewiring.sprite;

import alfredo.sprite.Bounds;
import alfredo.sprite.Drawable;
import alfredo.sprite.Force;

/**
 * An enemy with a minigun.
 * @author TheMonsterFromTheDeep
 */
public class Minigun extends Bounds implements Drawable {
    
    public static float HORIZONTAL_VELOCITY = 2.8f;
    
    public static int FIRE_COOLDOWN = 30;
    
    public int frame;
    public int cooldown = 0;
    
    public float yVelocity;
    public float yAcceleration;
    
    public float xVelocity;
    
    public Force force;
    
    public Minigun() {
        super(20, 22); //20 x 22 bounding box
        force = new Force(this, 0, 0);
        
        frame = 0;
        xVelocity = HORIZONTAL_VELOCITY;
    }
    
    @Override
    public float getDrawX() {
        return getWorldX() - 10;
    }

    @Override
    public float getDrawY() {
        return getWorldY() - 11;
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