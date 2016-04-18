package alfredo.sprite;

import alfredo.geom.Point;

/**
 * An Entity is some sort of object that has a position and direction in a local space.
 * 
 * Subclasses can override its various transform set methods in order to do more - for example,
 * a Bounds object will override them so that the bounding box will move with the Entity.
 * @author TheMonsterFromTheDeep
 */
public class Entity extends Transform {
    private Worldly parent;
    
    public Entity() {
        parent = World.world;
    }
    
    @Override
    public final float getWorldX() {
        return (float)(parent.getWorldX() + Math.cos(Math.toRadians(parent.getWorldDirection() + 90)) * location.y + Math.cos(Math.toRadians(parent.getWorldDirection())) * location.x);
    }
    
    @Override
    public final float getWorldY() {
        return (float)(parent.getWorldY() + Math.sin(Math.toRadians(parent.getWorldDirection() + 90)) * location.y + Math.sin(Math.toRadians(parent.getWorldDirection())) * location.x);
    }
    
    @Override
    public final double getWorldDirection() {
        return parent.getWorldDirection() + direction;
    }
    
    public final void setParent(Worldly p, boolean retainPosition) {
        if(p != null) {
            if(retainPosition) {
                //If direction is zero, inverse trig breaks; just use normal position though because direction is world direction too
                if(Math.abs(p.getWorldDirection()) < 0.0001) {
                    location.x = getWorldX() - p.getWorldX();
                    location.y = getWorldY() - p.getWorldY();
                }
                else {
                    //Literal algebraic inverse of the getWorldX(), getWorldY() functions - only difference is that x
                    //has to be temporarily stored because it is being set but is needed for the inverse of
                    //getWorldY()
                    float tempx = location.x;
                    double pdir = p.getWorldDirection(); //Makes this so much nicer
                    location.x = (float)((getWorldX() - p.getWorldX() - Math.cos(Math.toRadians(pdir + 90)) * location.y) / Math.cos(Math.toRadians(pdir)));
                    location.y = (float)((getWorldY() - p.getWorldY() - Math.sin(Math.toRadians(pdir)) * tempx) / Math.sin(pdir + 90));
                }
                direction = getWorldDirection() - p.getWorldDirection();
            }
            parent = p;
        }
    }
    
    public final void clearParent(boolean retainPosition) {
        if(retainPosition) {
            location.x = getWorldX();
            location.y = getWorldY();
            direction = getWorldDirection();
        }
        parent = World.world;
    }
}