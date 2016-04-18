package alfredo.geom;

public class Point {
    public float x, y;
    
    public Point() { x = 0; y = 0; }
    
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }
    
    /**
     * Translates the Point by the specified amount in the x/y plane.
     * @param dx The amount to translate the Point by horizontally.
     * @param dy The amount to translate the Point by vertically.
     */
    public void translate(float dx, float dy) {
        x += dx;
        y += dy;
    }
    
    /**
     * Gets the specified translation of this Point.
     * @param dx The x translation.
     * @param dy The y translation.
     * @return A Point that is a copy of this Point with the specified translation.
     */
    public Point getTranslation(float dx, float dy) {
        return new Point(x + dx, y + dy);
    }
    
    /**
     * Gets the specified translation of this Point and outputs it into the specified out Point.
     * @param dx The x translation.
     * @param dy The y translation.
     * @param out The Point to write the translation to.
     */
    public void getTranslation(float dx, float dy, Point out) {
        out.x = x + dx;
        out.y = y + dy;
    }
    
    /**
     * Copies the position stored by the specified Point to this Point.
     * @param p The Point to copy.
     */
    public void copyFrom(Point p) {
        this.x = p.x;
        this.y = p.y;
    }
    
    /**
     * Copy the specified location into this Point.
     * @param x The x coordinate to copy.
     * @param y The y coordinate to copy.
     */
    public void copyFrom(float x, float y) {
        this.x = x;
        this.y = y;
    }
}