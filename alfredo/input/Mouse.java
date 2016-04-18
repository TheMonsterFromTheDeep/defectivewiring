package alfredo.input;

import alfredo.geom.Point;
import alfredo.paint.Camera;
import alfredo.sprite.Entity;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Mouse {
    public class Handler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            switch(e.getButton()) {
                case MouseEvent.BUTTON1:
                    lmbDown = true;
                    break;
                case MouseEvent.BUTTON2:
                    mmbDown = true;
                    break;
                case MouseEvent.BUTTON3:
                    rmbDown = true;
                    break;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            switch(e.getButton()) {
                case MouseEvent.BUTTON1:
                    lmbDown = false;
                    break;
                case MouseEvent.BUTTON2:
                    mmbDown = false;
                    break;
                case MouseEvent.BUTTON3:
                    rmbDown = false;
                    break;
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            scrollTilt += e.getPreciseWheelRotation();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mousex = (int)((e.getX() - screenX) / screenScale);
            mousey = (int)((e.getY() - screenY) / screenScale);
            cursor.setX(camera.xToWorld(mousex));
            cursor.setY(camera.yToWorld(mousey));
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mousex = (int)((e.getX() - screenX) / screenScale);
            mousey = (int)((e.getY() - screenY) / screenScale);
            cursor.setX(camera.xToWorld(mousex));
            cursor.setY(camera.yToWorld(mousey));
        }
    }
    
    private boolean lmbDown;
    private boolean mmbDown;
    private boolean rmbDown;
    
    private double screenScale = 1; //Used to properly project mouse coordinates to the screen
    private int screenX = 0;
    private int screenY = 0;
    
    private int mousex, mousey;
    
    private double scrollTilt;
    
    private final Handler handler;
    
    private static Mouse mouse;
    
    private Camera camera;
    
    public final Entity cursor;
    
    private Mouse(Camera c) {
        lmbDown = false;
        mmbDown = false;
        rmbDown = false;
        
        mousex = mousey = 0;
        
        scrollTilt = 0;
        handler = new Handler();
        
        camera = c;
        cursor = new Entity();
    }
    
    public static Handler init(Camera c) {
        if(mouse == null) {
            mouse = new Mouse(c);
            return mouse.handler;
        }
        return null;
    }
    
    /**
     * Gets the Mouse with the specified Handler.
     * 
     * Meant to be used by the Game so that it can properly
     * update how the mouse updates its position.
     * @param handler The Handler to validate.
     * @return The static mouse object, if the Handler is valid; otherwise null.
     */
    public static Mouse getMouse(Handler handler) {
        if(handler == mouse.handler) {
            return mouse;
        }
        return null;
    }
    
    public void updateScreen(int screenX, int screenY, double scale) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.screenScale = scale;
    }
    
    public static void setCamera(Camera c) {
        mouse.camera = c;
    }
    
    public static boolean isLMBDown() { return mouse.lmbDown; }
    public static boolean isMMBDown() { return mouse.mmbDown; }
    public static boolean isRMBDown() { return mouse.rmbDown; }
    
    public static int getMouseX() { return mouse.mousex; }
    public static int getMouseY() { return mouse.mousey; }
    
    public static Point getPosition() { return new Point(mouse.mousex, mouse.mousey); }
    
    public static double getScrollTilt() { return mouse.scrollTilt; }
    public static void resetScrollTilt() { mouse.scrollTilt = 0; }
    
    public static Entity getCursor() { return mouse.cursor; }
}