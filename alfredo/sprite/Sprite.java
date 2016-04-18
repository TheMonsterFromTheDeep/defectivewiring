package alfredo.sprite;

import alfredo.geom.Point;
import alfredo.paint.Image;

/**
 * A Sprite is essentially a fully-featured Entity with a bounding box, a graphical appearance, and
 * just its raw position and direction.
 * 
 * Sprite is really only useful for single-instance objects with very simple graphics (only single images or
 * something similar...)
 * @author TheMonsterFromTheDeep
 */
public class Sprite extends Skeleton {
    Image image;
    
    public static Sprite loadFromPath(String path) {
        Image image = new Image();
        Image.load(path, image);
        return new Sprite(image);
    }
    
    public static Sprite loadFromPath(String path, Point center) {
        Image image = new Image();
        Image.load(path, image);
        return new Sprite(image, center);
    }
    
    public Sprite(Image image) {
        super(image);
        this.image = image;
    }

    public Sprite(Image image, Point center) {
        super(image, center);
        this.image = image;
    }
}