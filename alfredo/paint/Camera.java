package alfredo.paint;

public abstract class Camera {
    final int width, height;
    
    public Camera(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public Camera(Camera base) {
        this.width = base.width;
        this.height = base.height;
    }
    
    public Camera(Canvas base) {
        this.width = base.width;
        this.height = base.height;
    }
    
    //Return the world coordinate transformed into the screen
    public abstract float xToScreen(float x);
    public abstract float yToScreen(float y);
    
    //Return the screen coordinate transformed into the world
    public abstract float xToWorld(float x);
    public abstract float yToWorld(float y);
}