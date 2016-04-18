package alfredo.sprite;

import alfredo.geom.Intersection;
import alfredo.geom.Line;
import alfredo.geom.Point;
import alfredo.geom.Polygon;
import alfredo.input.Keys;

/**
 * A Force is a specialized object for sprite movement. It allows a Bounds object to move exactly to another Bounds object
 * based on a preexisting force. This is mainly for accurate collisions.
*/
public class Force {
    Line[] vectors;
    Point center; //The "new" center of the Force; e.g. the location that becomes a Bounds' location when apply() is called
    Bounds handle;
    
    float x, y; //The amount of x, y force that the Force is applying
    
    public Force(Bounds bounds, float dx, float dy) {
        bounds.sync(); //Bounds *needs* right state for this
        
        Point[] points = bounds.bounds.copyPoints();
        vectors = new Line[points.length];
        for(int i = 0; i < vectors.length; i++) {
            vectors[i] = new Line(points[i], points[i].getTranslation(dx, dy));
        }
        center = bounds.getLocation().getTranslation(dx, dy);
        
        handle = bounds; //The handle that this Force can be applied to.
        
        x = dx;
        y = dy;
    }
    
    /**
     * Simply initializes a Force object for use with a certain Bounds but does not actually create an applicable force.
     * @param bounds The relevant Bounds object.
     */
    public Force(Bounds bounds) {
        handle = bounds;
        vectors = new Line[bounds.bounds.points.length];
        for(int i = 0; i < vectors.length; i++) {
            vectors[i] = new Line();
        }
        center = new Point();
    }
    
    private static boolean equalPoints(Point a, Point b) {
        return (Math.abs(a.x - b.x) < 0.0001) && (Math.abs(a.y - b.y) < 0.0001);
    }
    
    /**
     * Causes the Force to "interact" with a Bounds.
     * 
     * Essentially, what this does is checks to see how the Force would cause its handle Bounds to
     * intersect with the specified Bounds object. If it would go inside it, (not having *originated*
     * inside it, of course) then the Force will modify itself to prevent that from happening.
     * 
     * Basically, if a Bounds has a Force applied that has interacted with other Bounds objects, the
     * applied Bounds will have accurate collisions to the extent of direction x/y movement.
     * 
     * It does not, however, interact in terms of other forces, including rotations and gravity.
     * @param b The Bounds to do an interaction with.
     * @return Whether the Force was modified - essentially, whether the parent bounds *is* in fact now "touching" the other object.
     */
    public boolean interact(Bounds b) {
        float checkdx = b.getWorldX() - handle.getWorldX(); //TODO: Fix all local/global position problems!
        float checkdy = b.getWorldY() - handle.getWorldY();
        
        //If the bounds interacting is in the opposite direction, don't interact
        if(((checkdx > 0) != (x > 0) || x == 0) && ((checkdy > 0) != (y > 0) || y == 0)) { return false; }
        //Should this be done with trig maybe?
        
        b.sync(); //Make sure bounds has position/rotation that it needs
        Polygon p = b.bounds; //Handle for convenience (compiler plz optimize)
        
        Intersection check = new Intersection(); //Passed to Line.intersects() because the intersection point is required for shifting the Force backwards
        boolean modified = false;
        
        for(Line vector : vectors) { //Iterate through lines to check for intersections
            for(int i = 0; i < p.lines.length; i++) {
                if(vector.intersects(p.lines[i], check)) { //The force is causing the parent Bounds to move into the new Bounds; take corrective action!
                    if(vector.parallel(p.lines[i])) {
                        continue;
                    }
                    int oLine = (i > 0) ? i - 1 : p.lines.length - 1;
                    if(vector.intersects(p.lines[oLine]) && vector.parallel(p.lines[oLine])) {
                        continue;
                    }
                    oLine = (i < p.lines.length - 1) ? i + 1 : 0;
                    if(vector.intersects(p.lines[oLine]) && vector.parallel(p.lines[oLine])) {
                        continue;
                    }
                    //Shorten the Force so that the interaction is pushed back; essentially, move *this* Line's point back
                    //to the intersection point with the other Bounds, and move all others back by an equal amount
                    if(check.type == Intersection.TYPE_POINT) { //Only modify if Intersection is point
                        float dx = check.point.x - vector.end.x;
                        float dy = check.point.y - vector.end.y;
                        for(Line l : vectors) { l.end.translate(dx, dy); } //Translate all lines for future interactions
                        center.translate(dx, dy); //translate the center
                        x += dx;
                        y += dy;
                        modified = true;
                    }
                }
            }
        }
        return modified;
    }
    
    /**
     * Applies the force to the Bounds object, causing it to take the new Position determined by the Force.
     */
    public void apply() {
        handle.setLocation(center);
    }
    
    /**
     * Causes the Force, using its old handle as the parent, to load itself with updated positions of its
     * parent so that it can be used for interaction again.
     * 
     * This prevents having to allocate new memory for new Points and new Lines.
     * @param dx The new x translation.
     * @param dy The new y translation.
     */
    public void reforce(float dx, float dy) {
        handle.sync(); //Bounds *needs* right box position for this
        for(int i = 0; i < vectors.length; i++) {
            vectors[i].start.copyFrom(handle.bounds.points[i]);
            handle.bounds.points[i].getTranslation(dx, dy, vectors[i].end);
        }
        handle.getLocation().getTranslation(dx, dy, center);
        
        x = dx;
        y = dy;
    }
    
    public void reforce(Bounds b, float dx, float dy) {
        handle = b;
        handle.sync(); //Bounds *needs* right box position for this
        vectors = new Line[handle.bounds.points.length];
        for(int i = 0; i < vectors.length; i++) {
            vectors[i] = new Line(handle.bounds.points[i], handle.bounds.points[i].getTranslation(dx, dy));
        }
        handle.getLocation().getTranslation(dx, dy, center);
        
        x = dx;
        y = dy;
    }
}