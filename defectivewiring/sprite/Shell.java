package defectivewiring.sprite;

import alfredo.sprite.Drawable;
import alfredo.sprite.Entity;

/**
 * "Bullets" for the Minigun, because apparently only the Robot can shoot Bullets.
 * @author TheMonsterFromTheDeep
 */
public class Shell extends Entity implements Drawable {

    public static final int LIFETIME = 60;
    public static final float VELOCITY_X = 7;
    
    public float velX;
    public float velY;
    
    public int lifetime;
    
    public final byte frame;
    
    public Shell(float velX, float velY) {
        this.velX = velX;
        this.velY = velY;
        
        frame = (byte) (velX < 0 ? 1 : 0);
        
        lifetime = LIFETIME;
    }
    
    @Override
    public float getDrawX() {
        return getWorldX() - 3.5f;
    }

    @Override
    public float getDrawY() {
        return getWorldY() - 2;
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