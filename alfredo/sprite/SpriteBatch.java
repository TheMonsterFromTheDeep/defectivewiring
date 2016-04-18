package alfredo.sprite;

import alfredo.paint.Canvas;
import alfredo.paint.Image;
import java.awt.Color;

public class SpriteBatch {
    public Canvas canvas;
    public Color background;
    
    public static final int DEFAULT_COLOR = 0x9ecae8; //Default background color, skyish blue
    public static final Color DEFAULT_INK = new Color(0);

    public SpriteBatch(Color color) {
        background = color;
    }
    
    public SpriteBatch() { this(new Color(DEFAULT_COLOR)); }
    
    public void begin(Canvas c) {
        canvas = c;
        canvas.fill(background);
    }
    
    public void drawEntity(Image i, Entity e) {
        canvas.draw(i, e.getWorldX(), e.getWorldY(), e.getWorldDirection(), e.getWorldX(), e.getWorldY());
    }
    
    public void draw(Image i, Drawable d) {
        canvas.draw(i, d.getDrawX(), d.getDrawY(), d.getDrawDirection(), d.getDrawPivotX(), d.getDrawPivotY());
    }
    
    public void draw(Sprite s) {
        draw(s.image, s); //Draw the Sprite's image onto the Sprite
    }
    
    /**
     * Draws a wireframe picture of the specified bounding box in the specified color.
     * 
     * Will synchronize the bounding box.
     * @param b The bounding box to draw.
     * @param c The color to draw the bounding box in.
     */
    public void ink(Bounds b, Color c) {
        b.sync();
        canvas.ink(b.bounds, c);
    }
    
    /**
     * Draws a wireframe picture of the specified bounding box in the default ink color, which
     * is black.
     * 
     * Will synchronize the bounding box.
     * @param b The bounding box to draw.
     */
    public void ink(Bounds b) {
        b.sync();
        canvas.ink(b.bounds, DEFAULT_INK);
    }
    
    /**
     * Draws a wireframe picture of the specified bounding box in the default ink color, which
     * is black.
     * 
     * Will synchronize the bounding box.
     * 
     * This particular method also includes an image argument which is not used. This is simply so that
     * this method can be used for debugging - a draw() method can be easily changed to an ink() method and vice
     * versa.
     * @param i Throwaway parameter.
     * @param b The bounding box to draw.
     */
    public void ink(Image i, Bounds b) {
        ink(b);
    }
    
    /**
     * Draws a wireframe picture of the specified bounding box in the specified color.
     * 
     * Will synchronize the bounding box.
     * 
     * This particular method also includes an image argument which is not used. This is simply so that
     * this method can be used for debugging - a draw() method can be easily changed to an ink() method and vice
     * versa.
     * 
     * Of course, in this particular instance, this method is mostly just for slightly easier transition, as the
     * draw() method does not have a Color parameter.
     * @param i Throwaway parameter.
     * @param b The bounding box to draw.
     * @param c The color to draw the bounding box in.
     */
    public void ink(Image i, Bounds b, Color c) {
        ink(b,c);
    }
}