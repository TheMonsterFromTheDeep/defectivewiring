package alfredo.sprite;

import alfredo.geom.Point;

/**
 * A Transform encapsulates a position and a direction.
 * 
 * The position is a Point and can be used as such, while the
 * direction is a double for maximum accuracy.
 * @author TheMonsterFromTheDeep
 */
public class Transform implements Worldly {
    protected Point location; //Protected so that they aren't rashly modified (needed for some entities)
    protected double direction;

    public Transform() {
        location = new Point();
        direction = 0;
    }
    
    @Override
    public float getWorldX() {
        return location.x;
    }

    @Override
    public float getWorldY() {
        return location.y;
    }

    @Override
    public double getWorldDirection() {
        return direction;
    }
    
    public void setX(float x) {
        location.x = x;
    }
    
    public void setY(float y) {
        location.y = y;
    }
    
    public void setDirection(double direction) {
        this.direction = direction;
    }
    
    public float getX() { return location.x; }
    public float getY() { return location.y; }
    public double getDirection() { return direction; }
    
    public final void moveX(float amount) {
        setX(location.x + amount);
    }
    
    public final void moveY(float amount) {
        setY(location.y + amount);
    }
    
    public final void move(float amount) {
        setX(location.x + (float)(Math.cos(Math.toRadians(direction + 90)) * amount));
        setY(location.y + (float)(Math.sin(Math.toRadians(direction + 90)) * amount));
    }
    
    public final void rotate(double direction) {
        setDirection(this.direction + direction);
    }
    
    public final void pointTowards(float x, float y) {
        setDirection(Math.toDegrees(Math.atan2(y - location.y, x - location.x)) - 90);
    }
    
    public final void pointTowards(Point p) {
        pointTowards(p.x, p.y);
    }
    
    /**
     * Returns the Transform's location through a copy. This prevents the original transform
     * from being set without the setter methods.
     * @return The Transform's position.
     */
    public final Point getLocation() {
        return new Point(location.x, location.y);
    }
    
    public final void setLocation(float x, float y) {
        setX(x);
        setY(y);
    }
    
    /**
     * Copies the specified Point's location as the new location of this Transform.
     * @param p The new location of the Transform.
     */
    public final void setLocation(Point p) {
        setX(p.x);
        setY(p.y);
    }
    
    public final float distanceTo(Point p) {
        float x = (getWorldX() - p.x);
        float y = (getWorldY() - p.y);
        return (float)Math.sqrt(x * x + y * y);
    }
}