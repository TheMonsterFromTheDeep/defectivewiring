package alfredo.geom;

import alfredo.input.Keys;

public class Line {
    public Point start;
    public Point end;
    
    public Line() {
        start = new Point();
        end = new Point();
    }
    
    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
    
    public Line(float sx, float sy, float ex, float ey) {
        start = new Point(sx, sy);
        end = new Point(ex, ey);
    }
    
    public boolean horizontal() { return Math.abs(end.y - start.y) < 0.001; }
    public boolean vertical() { return Math.abs(end.x - start.x) < 0.001; }
    
    public float slope() {
        if(vertical()) { throw new IllegalArgumentException("[Line.slope] Cannot return slope if vertical! Check for verticality first."); }
        return (start.y - end.y) / (start.x - end.x);
    }
    
    /**
     * 
     * @return The y-intercept of the line.
     */
    public float y_intercept() {
        return (-start.x * slope()) + start.y;
    }
    
    public boolean parallel(Line l) {
        if(l.vertical() || this.vertical()) {
            return (l.vertical() && this.vertical());
        }
        return areEqual(this.slope(), l.slope());
    }
    
    /**
     * Translate the Line by the specified amount.
     * @param dx The amount to translate the Line horizontally.
     * @param dy The amount to translate the Line vertically.
     */
    public void translate(float dx, float dy) {
        start.translate(dx, dy);
        end.translate(dx, dy);
    }
    
    /**
     * Gets the y coordinate of a point along this Line with the specified x coordinate.
     * 
     * Will return a value regardless of whether the point is within bounds.
     * @param x The x coordinate of the point.
     * @return The y coordinate of the the point.
     */
    public float getY(float x) {
        return y_intercept() + (x * slope());
    }
    
    private static boolean areEqual(float a, float b) {
        return Math.abs(a - b) < 0.0001;
    }
    
    private static boolean greaterOrEqual(float a, float b) {
        return areEqual(a, b) || a > b;
    }
    
    private static boolean lessOrEqual(float a, float b) {
        return areEqual(a, b) || a < b;
    }
    
    /**
     * 
     * @param check The point to check.
     * @return Whether the point is inside the bounds of the line.
     */
    public boolean bounds(Point check) {
        return ((greaterOrEqual(check.x, start.x) && lessOrEqual(check.x, end.x)) || (greaterOrEqual(check.x, end.x) && lessOrEqual(check.x, start.x))) &&
               ((greaterOrEqual(check.y, start.y) && lessOrEqual(check.y, end.y)) || (greaterOrEqual(check.y, end.y) && lessOrEqual(check.y, start.y)));
    }
       
    public boolean intersects(Line check) {
        /*Point test = new Point(); //The point to test for
        if(vertical()) {
            if(check.vertical()) {
                return check.bounds(start) || check.bounds(end);
            }
            test.x = start.x;
            test.y = check.getY(test.x);
        }
        else if(check.vertical()) {
            test.x = check.start.x;
            test.y = getY(test.x);
        }
        else {
            float mySlope = slope();
            float checkSlope = check.slope();
            test.x = ((checkSlope * check.start.x) + check.start.y + (mySlope * start.x) - start.y) / (mySlope - checkSlope);
            test.y = getY(test.x);
        }
        return bounds(test) && check.bounds(test);*/
        return intersects(check, null); //More elegant solution in future maybe?
        
        //int xdif1 = end.x - start.x;
        //int xdif2 = check.end.x - check.start.x;
        
        /*if(xdif1 == 0 || xdif2 == 0) {
            System.out.println("0 slope branch");
            if(xdif1 == xdif2) {
                System.out.println("all zeroes");
                return ((start.y >= check.start.y && start.y <= check.end.y) || (start.y <= check.start.y && start.y >= check.end.y));
            }
            if(xdif1 == 0) {
                return ((start.x >= check.start.x && start.x <= check.end.x) || (start.x <= check.start.x && start.x >= check.end.x));
            }
            else {
                return ((check.start.x >= start.x && check.start.x <= end.x) || (check.start.x <= start.x && check.start.x >= end.x));
            }
        }
        else if (xdif1 == xdif2) {
            System.out.println("Parallel branch");
            return ((start.x >= check.start.x && start.x <= check.end.x) || (start.x <= check.start.x && start.x >= check.end.x));
        }
        else {
            System.out.println("Normal branch");
            float m1, m2, checkx;
            
            m1 = (float)(end.y - start.y) / xdif1;
            m2 = (float)(check.end.y - check.start.y) / xdif2;
            checkx = ((check.start.y - (m2 * check.start.x)) - (start.y - (m1 * start.x))) / (m1 - m2);
            return ((checkx >= check.start.x && checkx <= check.end.x) || (checkx <= check.start.x && checkx >= check.end.x));
        }*/
        
    }
    
    /**
     * Checks for an intersection with another Line, and if it finds it, writes the
     * intersection to the specified Point.
     * 
     * Can be used in the interest of good style; requires only looking for intersection
     * point *once.*
     * @param check The Line to check intersection with.
     * @param out The Point to write the intersection to.
     * @return Whether the lines intersect.
     */
    public boolean intersects(Line check, Intersection out) {
        //TODO: Figure out how to not do this with copy-pasted code from non-writing method (check?)
        Point test = new Point(); //The point to test for
        if(vertical()) {
            if(check.vertical()) {
                if(out != null) {
                    out.type = Intersection.TYPE_LINE;
                }
                return check.bounds(start) || check.bounds(end);
            }
            test.x = start.x;
            test.y = check.getY(test.x);
        }
        else if(check.vertical()) {
            test.x = check.start.x;
            test.y = getY(test.x);
        }
        else {
            float mySlope = slope();
            float checkSlope = check.slope();
            if(areEqual(mySlope, checkSlope)) {
                if(out != null) {
                    out.type = Intersection.TYPE_LINE;
                }
                return check.bounds(start) || check.bounds(end);
            }
            test.x = ((checkSlope * check.start.x) + check.start.y + (mySlope * start.x) - start.y) / (mySlope - checkSlope);
            test.y = getY(test.x);
        }
        if (bounds(test) && check.bounds(test)) { //If the point *is* an intersection, output it as such and return true
            if(out != null) {
                out.type = Intersection.TYPE_POINT;
                out.point = test;
            }
            return true;
        }
        return false; //Point is not intersection
    }
}