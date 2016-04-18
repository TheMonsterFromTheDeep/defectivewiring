package defectivewiring.sprite;

import alfredo.sprite.Drawable;
import alfredo.sprite.Entity;

public class Bullet extends Entity implements Drawable {

    public int lifetime;
    public int currentFrame;
    
    public int velocity; //bullets only move horizontally :]
    
    public Bullet(int lifetime, int velocity) {
        this.lifetime = lifetime;
        currentFrame = 0;
        
        this.velocity = velocity;
    }
    
    public void update() {
        --lifetime;
        ++currentFrame;
        if(currentFrame > 4) { //last possible frame
            currentFrame = 1; //First frame that isn't growing
        }
        
        moveX(velocity);
    }
    
    @Override
    public float getDrawX() {
        return getWorldX() - 4;
    }

    @Override
    public float getDrawY() {
        return getWorldY() - 4;
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