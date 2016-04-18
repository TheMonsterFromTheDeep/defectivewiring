package alfredo.sprite;

import alfredo.geom.Point;
import alfredo.geom.Polygon;

/**
 * A Bounds object represents a bounding box of some sort.
 * @author TheMonsterFromTheDeep
 */
public class Bounds extends Entity {
    /**
     * The default object used to initialize the Bounds object. This allows it to sync even
     * if a new shape is never specified.
     */
    public static final Polygon DEFAULT_BOUNDS = new Polygon(new Point[] { new Point(0, 0) }); 
    
    private volatile Polygon base;
    public Polygon bounds; //The modified (translated, rotated, etc) version of the bounds data
    
    private Point lastSyncPoint;
    private double lastSyncDir;
    
    protected final void setShape(Polygon p) {
        base = p;
        bounds = base.copy();
        sync(true);
    }
    
    protected Bounds() {
        location = new Point(0, 0);
        lastSyncPoint = new Point(0, 0);
        lastSyncDir = 0;
        setShape(DEFAULT_BOUNDS); //NOTE: If sync() is no longer called in setShape, please add it to this constructor
    }
    
    public Bounds(Polygon p) {
        this();
        setShape(p);
    }
    
    /**
     * Initializes the Bounds by centering the Polygon on the specified point.
     * @param p The Polygon that forms the Bounds
     * @param center The Point that the Polygon should be centered on.
     */
    public Bounds(Polygon p, Point center) {
        this();
        base = p;
        base.translate(-center.x, -center.y);
        setShape(base);
    }
    
    public Bounds(float width, float height) {
        this();
        float hWidth = width / 2;
        float hHeight = height / 2;
        setShape(new Polygon(new Point[] { new Point(-hWidth, -hHeight), new Point(hWidth, -hHeight), new Point(hWidth, hHeight), new Point(-hWidth, hHeight), new Point(-hWidth, -hHeight) }));
    }
    
    public Bounds(float width, float height, Point center) {
        this();
        float hWidth = width / 2;
        float hHeight = height / 2;
        setShape(new Polygon(new Point[] { new Point(-hWidth + center.x, -hHeight + center.y), new Point(hWidth + center.x, -hHeight + center.y), new Point(hWidth + center.x, hHeight + center.y), new Point(-hWidth + center.x, hHeight + center.y) }));
    }
    
    @Override
    public void setX(float x) {
        //bounds.translate(x - location.x, 0);
        location.x = x;
    }
    
    @Override
    public void setY(float y) {
        //bounds.translate(0, y - location.y);
        location.y = y;
    }
    
    @Override
    public void setDirection(double dir) {
        //double oldDirection = direction;
        direction = dir;
        //bounds.rotate(oldDirection - direction, getWorldX(), getWorldY());
    }
    
    /**
     * A specialized transform copy method which cleans the Bound's transform's inaccuracies
     * by copying from the base polygon that forms the Bounds.
     * @param target The Transform to copy.
     */
    public void cleanCopyTransform(Transform target) {
        bounds.copyFrom(base); //Reset position, direction
        
        this.direction = target.direction; //Copy target direction, position
        this.location = target.location;
        
        bounds.rotate(direction, getX(), getY());
        bounds.translate(location.x, location.y); //Move to correct position
    }
    
    /**
     * Applies the global transformation of the Bounds object to its actual Polygon data.
     * 
     * This is called whenever the Bounds does an intersection check so that the Bounds has
     * the proper and correct data for everything that it is doing.
     * 
     * This method is optimized by checking whether this object has already been synced. However,
     * due to the nature of how this works, the object will still think it has been synced if something
     * like the shape changes. By setting the ignorePrevious flag to true, the method will not perform
     * this optimization and will sync no matter what.
     * @param ignorePrevious Whether the Bounds object should skip checking whether it is already synchronized.
     */
    public final void sync(boolean ignorePrevious) {
        float x = getWorldX();
        float y = getWorldY();
        
        //TODO: Implement global comparison methods
        //Only sync if the Bounds has moved significantly from where it was previously synced
        if(!ignorePrevious || Math.abs(lastSyncPoint.x - x) < 0.0001 && Math.abs(lastSyncPoint.y - y) < 0.0001 && Math.abs(lastSyncDir - getWorldDirection()) < 0.0001) {
            bounds.copyFrom(base);

            bounds.rotate(getWorldDirection(), 0, 0);
            bounds.translate(x, y); //Translate the bounds object to have global position (this should work... right?)
            
            lastSyncPoint.copyFrom(x, y);
            lastSyncDir = getWorldDirection();
        }
    }
    
    /**
     * Applies the global transformation of the Bounds object to its actual Polygon data.
     * 
     * This is called whenever the Bounds does an intersection check so that the Bounds has
     * the proper and correct data for everything that it is doing.
     */
    public final void sync() {
        sync(false);
    }
    
    /**
     * Determines whether this Bounds is touching the specified Bounds in any way - either
     * they are intersecting or one contains the other.
     * 
     * This uses the bounds of the Bounds to check for intersections.
     * @param b The Bounds to check with.
     * @return Whether the Bounds are touching.
     */
    public final boolean touches(Bounds b) {
        sync(); //Make sure bounds position/rotation is good
        b.sync(); //Make sure *other* bounds object is synced
        return bounds.intersects(b.bounds) || bounds.contains(b.bounds) || b.bounds.contains(bounds);
    }
}