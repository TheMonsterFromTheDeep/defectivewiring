package alfredo.paint;

import alfredo.geom.Point;
import alfredo.sprite.Worldly;

public class WorldCamera extends Camera {

    public Point location;
    
    public WorldCamera(int width, int height) {
        super(width, height);
        
        location = new Point(0, 0);
    }

    public WorldCamera(Camera camera) {
        this(camera.width, camera.height);
    }
    
    public WorldCamera(Canvas canvas) {
        this(canvas.width, canvas.height);
    }

    @Override
    public float xToScreen(float x) {
        return (x - location.x);
    }

    @Override
    public float yToScreen(float y) {
        return (y - location.y);
    }

    @Override
    public float xToWorld(float x) {
        return (x + location.x);
    }

    @Override
    public float yToWorld(float y) {
        return (y + location.y);
    }
    
    /**
     * Tracks the Camera to the specified Worldly so that it is in the center of the screen.
     * @param w The Worldly to track to.
     */
    public void track(Worldly w) {
        location.x = w.getWorldX();
        location.y = w.getWorldY();
    }
    
    public void setX(float x) {
        location.x = x;
    }
    
    public void setY(float y) {
        location.y = y;
    }
    
}