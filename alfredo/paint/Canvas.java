package alfredo.paint;

import alfredo.geom.Line;
import alfredo.geom.Polygon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class Canvas {
    BufferedImage buffer;
    Graphics graphics;
    
    public final int width;
    public final int height;
    
    private final float offsetx;
    private final float offsety;
    
    Camera camera;
    
    private float translateX(float x) {
        return camera.xToScreen(x) + offsetx;
    }
    
    private float translateY(float y) {
        return camera.yToScreen(y) + offsety;
    }
    
    //#hax
    private static int myRound(float i) {
        return Math.round(Math.round(i * 100) / 100f);
    }
    
    private int intTranslateX(float x) {
        return (int)myRound(translateX(x));
    }
    
    private int intTranslateY(float y) {
        return (int)myRound(translateY(y));
    }
    
    public Canvas(int width, int height) {
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = buffer.createGraphics();
        
        this.width = width;
        this.height = height;
        
        this.offsetx = width / 2f;
        this.offsety = height / 2f;
        
        camera = new StaticCamera(width, height);
    }
    
    public void render(Graphics g, int x, int y, int width, int height) {
        g.drawImage(buffer, x, y, width, height, null);
    }
    
    public void fill(Color c) {
        graphics.setColor(c);
        graphics.fillRect(0, 0, width, height);
    }
    
    public void draw(Image i, float x, float y) {
        //graphics.drawImage(i.image, (int)translateX(x), (int)translateY(y), null);
        graphics.drawImage(i.image, intTranslateX(x), intTranslateY(y), null);
    }
    
    /**
     * Draws the graphic with the specified rotation.
     * 
     * This method simply rotates at the image center.
     * @param g Graph
     * @param x
     * @param y
     * @param angle Rotation in degrees.
     */
    public void draw(Image i, float x, float y, double angle) {
        draw(i, x, y, angle, i.image.getWidth() / 2f, i.image.getHeight() / 2f);
    }
    
    public void draw(Image i, float x, float y, double angle, float pivotx, float pivoty) {
        BufferedImage orig = i.image;
        
        int w = orig.getWidth();
        int h = orig.getHeight();
        
        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(angle), pivotx, pivoty);
        
        //Based off of http://stackoverflow.com/questions/10426883/affinetransform-truncates-image
        
        Point[] points = new Point[] {
            new Point(0, 0),
            new Point(w, 0),
            new Point(w, h),
            new Point(0, h)
        };
        
        at.transform(points, 0, points, 0, 4);
        
        Point min = new Point(points[0]);
        Point max = new Point(points[0]);
        
        for(Point p : points) {
            int px = p.x, py = p.y;
            
            if(px < min.x) { min.x = px; }
            else if(px > max.x) { max.x = px; }
            
            if(py < min.y) { min.y = py; }
            else if(py > max.y) { max.y = py; }
        }
        
        BufferedImage filter = new BufferedImage(max.x - min.x, max.y - min.y, orig.getType());
        
        at.preConcatenate(AffineTransform.getTranslateInstance(-min.x, -min.y));
        
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        op.filter(orig, filter);
        
        //graphics.drawImage(filter, (int)translateX(x + min.x), (int)translateY(y + min.y), null);
        graphics.drawImage(filter, intTranslateX(x + min.x), intTranslateY(y + min.y), null);
    }
    
    public void ink(Polygon p, Color c) {
        graphics.setColor(c);
        for(Line l : p.lines) {
            //graphics.drawLine((int)translateX(l.start.x), (int)translateY(l.start.y), (int)translateX(l.end.x), (int)translateY(l.end.y));
            graphics.drawLine(intTranslateX(l.start.x), intTranslateY(l.start.y), intTranslateX(l.end.x), intTranslateY(l.end.y));
        }
    }
    
    public void setCamera(Camera c) {
        if(c.width == width && c.height == height) {
            camera = c;
        }
    }
    
    public Camera getCamera() { return camera; }
    
    public BufferedImage getRender() {
        return buffer;
    }
}