package alfredo.sprite;

/**
 * A Worldly is an object that has a meaningful Transform in the World.
 * 
 * The World itself is a Worldly with a Transform of zero.
 * @author TheMonsterFromTheDeep
 */
public interface Worldly {
    float getWorldX();
    float getWorldY();
    double getWorldDirection();
}