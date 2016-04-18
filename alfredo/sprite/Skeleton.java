package alfredo.sprite;

import alfredo.geom.Point;
import alfredo.geom.Polygon;
import alfredo.paint.Image;
import alfredo.util.Resources;
import java.awt.image.BufferedImage;

/**
 * A Skeleton associates a Bounds object with a Drawable and holds them consistent. Itself, a Skeleton has
 * no graphical data - it simply contains the logical data that interacts virtually, while extra graphical
 * data has to be provided to a drawing method in order for a Skeleton to have any graphical meaning.
 * @author TheMonsterFromTheDeep
 */
public class Skeleton extends Bounds implements Drawable {    
    float offsetx, offsety; //The offset for where the image is drawn
    
    public static Skeleton loadFromPath(String path) {
        Image image = new Image();
        Image.load(path, image);
        return new Skeleton(image);
    }
    
    public static Skeleton loadFromPath(String path, Point center) {
        Image image = new Image();
        Image.load(path, image);
        return new Skeleton(image, center);
    }
    
    public Skeleton(Image image, Point center) {        
        float halfWidth = image.image.getWidth() / 2.0f;
        float halfHeight = image.image.getHeight() / 2.0f;
        
        offsetx = center.x - halfWidth;
        offsety = center.y - halfHeight;
        
        //By default, construct a square bounding box
        //The box is constructed such that the center point is empirically equal to 0,0.
        //Essentially, the bounding box is created so that 0,0 is offset from the middle of
        //the box to the point called "center".
        
        //Does that make sense?
        //Here's a picture:
        // |-----|
        // |     |
        // |     |
        // |  b  |
        // |     |
        // |    o|
        // |-----|
        // b is the middle of the bounding box, while o is 0,0 in coordinate space. In this case, the "center" point
        // was supposed to be (2,2), so o is offset from b by 2 in both directions.
        setShape(new Polygon(new Point[] { new Point(-halfWidth + center.x, -halfHeight + center.y), new Point(halfWidth + center.x, -halfHeight + center.y), new Point(halfWidth + center.x, halfHeight + center.y), new Point(-halfWidth + center.x, halfHeight + center.y), new Point(-halfWidth + center.x, -halfHeight + center.y)}));
    }
    
    public Skeleton(Image image) { this(image, new Point(0, 0)); }

    @Override
    public float getDrawX() {
        return getWorldX() + offsetx;
    }
    @Override
    public float getDrawY() {
        return getWorldY() + offsety;
    }

    @Override
    public float getDrawPivotX() {
        return getWorldX() - offsetx;
    }
    @Override
    public float getDrawPivotY() {
        return getWorldY() - offsety;
    }

    @Override
    public double getDrawDirection() { return getWorldDirection(); }
}