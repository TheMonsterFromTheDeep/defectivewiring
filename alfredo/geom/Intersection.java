package alfredo.geom;

public class Intersection {
    public static final byte TYPE_POINT = 0;
    public static final byte TYPE_LINE = 1;
    
    public Point point;
    public byte type;
    
    //Constructor for when the Intersection is a line
    public Intersection() {
        point = new Point();
        type = TYPE_LINE; 
    }
    
    //Constructor for point intersection
    public Intersection(Point p) {
        point = p;
        type = TYPE_POINT;
    }
}