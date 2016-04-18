package alfredo.sprite;

/**
 * A Drawable provides data for how an object can be drawn. Essentially, a Drawable
 * object is used to tell where an image is drawn - which is not necessarily where
 * a representative Entity or other object is.
 * 
 * All the methods in a Drawable start with "getDraw" for convenience - they very clearly
 * separate them from other position / direction methods that a class may contain.
 * @author TheMonsterFromTheDeep
 */
public interface Drawable {
    float getDrawX();
    float getDrawY();
    float getDrawPivotX();
    float getDrawPivotY();
    double getDrawDirection();
}