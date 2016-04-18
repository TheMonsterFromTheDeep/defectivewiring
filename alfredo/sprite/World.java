package alfredo.sprite;

/**
 * The World is the highest-level parent for all other objects.
 * 
 * It has all transforms of zero.
 * @author TheMonsterFromTheDeep
 */
public class World implements Worldly {
    public static World world;
    
    public static void init() {
        if(world == null) {
            world = new World();
        }
    }
    
    @Override public float getWorldX() { return 0; }
    @Override public float getWorldY() { return 0; }
    @Override public double getWorldDirection() { return 0; }
}