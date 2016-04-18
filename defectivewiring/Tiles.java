package defectivewiring;

import alfredo.geom.Point;
import alfredo.sprite.Bounds;
import alfredo.sprite.Force;

/**
 * The tile-based world for the characters.
 * @author TheMonsterFromTheDeep
 */
public class Tiles {
    byte[][] data;
    
    int centerx;
    int centery;
    
    Bounds[] boxes;
    
    public Tiles(byte[][] data, Bounds[] boxes) {
        this.data = data;
        
        centerx = data.length / 2;
        centery = data[0].length / 2;
        
        this.boxes = boxes;
    }
    
    public void init() {
        centerx = data.length / 2;
        centery = data[0].length / 2;
    }
    
    /**
     * Projects World x into Tiles x.
     * @param position
     * @return 
     */
    public int getTileX(Point position) {
        return (int)(position.x / 16) - ((position.x < 0) ? 1 : 0);
    }
    
    public int getTileX(float x) {
        return (int)(x / 16) - ((x < 0) ? 1 : 0);
    }
    
    public int getTileY(Point position) {
        return (int)(position.y / 16) - ((position.y < 0) ? 1 : 0);
    }
    
    public int getTileY(float y) {
        return (int)(y / 16) - ((y < 0) ? 1 : 0);
    }
    
    public int getDataX(float x) {
        return getTileX(x) + centerx;
    }
    
    public int getDataY(float y) {
        return getTileY(y) + centery;
    }
    
    public int getTileByRawX(int tileX) {
        return tileX - centerx;
    }
    
    public int getTileByRawY(int tileY) {
        return tileY - centery;
    }
    
    public float getWorldX(int tileX) {
        return tileX * 16;
    }
    
    public float getWorldY(int tileY) {
        return tileY * 16;
    }
    
    public void setTile(int x, int y, byte b) {
        data[x + centerx][y + centery] = b;
    }
    
    
    
    /*public void checkTile(Force f, Bounds b, int x, int y, byte expected) {
        if(getTileByTile(x, y) == expected) { //TODO: Much better
            b.setX(getWorldX(x));
            b.setY(getWorldY(y));
            f.interact(b);
        }
    }*/
    
    public boolean checkTile(Force f, int x, int y) {
        //TODO: Much better
        Bounds b = boxes[getTileByTile(x, y)];
        b.setX(getWorldX(x));
        b.setY(getWorldY(y));
        return f.interact(b);
        
    }
    
    public boolean checkTile(Bounds object, int x, int y) {
        //TODO: Much better
        Bounds b = boxes[getTileByTile(x, y)];
        b.setX(getWorldX(x));
        b.setY(getWorldY(y));
        return object.touches(b);
        
    }
    
    /**
     * Projects the World point into the Tiles system and gets the specified tile.
     * @param position
     * @return 
     */
    public byte getTileByWorld(Point position) {
        return data[getTileX(position) + centerx][getTileY(position) + centery];
    }
    
    /**
     * Gets the tile at the specified x and y.
     * @param x
     * @param y
     * @return 
     */
    public byte getTileByTile(int x, int y) {
        //return sky if tile is out of bounds
        if(x + centerx < 0 || x + centerx >= data.length || y + centery < 0 || y + centery >= data[0].length) { return Main.TILE_WALL_DARK; }
        return data[x + centerx][y + centery];
    }
}