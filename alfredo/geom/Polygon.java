package alfredo.geom;

public class Polygon {
    public Point[] points;
    public Line[] lines;
    
    Rectangle bounds;
    
    private void calculateBounds() {
        bounds = new Rectangle(0, 0, 0, 0);
        for(Point p : points) {
            if(p.x < bounds.left()) { bounds.setLeft(p.x); }
            if(p.x > bounds.right()) { bounds.setRight(p.x); }
            if(p.y < bounds.top()) { bounds.setTop(p.y); }
            if(p.y > bounds.bottom()) { bounds.setBottom(p.y); }
        }
    }
    
    public Polygon(Point[] points) {
        this.points = points;
        
        //TODO: IMPLEMENT CHANGES ELSEWHERE
        lines = new Line[points.length];// - 1];
        for(int i = 0; i < lines.length - 1; i++) {
            lines[i] = new Line(points[i], points[i + 1]);
        }
        if(lines.length > 0) {
            lines[lines.length - 1] = new Line(points[points.length - 1], points[0]);
        }
        
        calculateBounds();
    }
    
    private Polygon(Point[] p, Line[] l, Rectangle r) {
        this.points = p;
        this.lines = l;
        this.bounds = r;
    }
    
    public boolean contains(Point p) {
        //Thanks to http://stackoverflow.com/questions/217578/how-can-i-determine-whether-a-2d-point-is-within-a-polygon#answer-2922778
        int i, j;
        boolean c = false;
        for (i = 0, j = points.length - 1; i < points.length; j = i++) {
          if ( ((points[i].y > p.y) != (points[j].y > p.y)) &&
            (p.x < (points[j].x-points[i].x) * (p.y-points[i].y) / (points[j].y-points[i].y) + points[i].x) )
            { c = !c; }
        }
        return c;
    }
    
    /**
     * Returns whether any of the edges of this Polygon are intersecting with
     * the edges of the specified Polygon.
     * 
     * This does not return true if one Polygon is merely contained within the other,
     * but not actually intersecting.
     * @param check The Polygon to check intersection with.
     * @return Whether the Polygons are intersecting.
     */
    public boolean intersects(Polygon check) {
        if(!bounds.intersects(check.bounds)) { return false; }

        for(Line line1 : lines) {
            for(Line line2 : check.lines) {
                if(line1.intersects(line2)) { return true; }
            }
        }

        
        return false;
    }
    
    /**
     * Returns whether this Polygon contains any point in the specified Polygon.
     * 
     * This does not test for whether a Polygon is *completely* contained, only
     * if it is at least partially contained.
     * @param check The Polygon to check.
     * @return Whether this Polygon contains any part of the specified Polygon.
     */
    public boolean contains(Polygon check) {
        for(Point p : check.points) {
            if(contains(p)) { return true; }
        }
        
        return false;
    }
    
    /**
     * Rotates the polygon around the specified anchor point by the
     * specified number of degrees.
     * 
     * This method modifies the Polygon in-place.
     * @param degrees The amount, in degrees, to rotate the polygon
     * @param ax The anchor point x
     * @param ay The anchor point y
     */
    public void rotate(double degrees, float ax, float ay) {
        //IMPORTANT: It is not necessary to update the Lines because they all contain a direct pointer to the points.
        for(Point point : points) {
            float ydist = point.y - ay;
            float xdist = point.x - ax;
            double angle = Math.atan2(ydist, xdist);
            double distance = Math.sqrt(xdist * xdist + ydist * ydist);
            point.x = (float)(ax + (Math.cos(angle + Math.toRadians(degrees)) * distance));
            point.y = (float)(ay + (Math.sin(angle + Math.toRadians(degrees)) * distance));
        }
        calculateBounds();
    }
    
    /**
     * Translates the polygon by the specified amount.
     * @param dx The amount to translate the Polygon along the x axis.
     * @param dy The amount to translate the Polygon along the y axis.
     */
    public void translate(float dx, float dy) {
        //IMPORTANT: It is not necessary to update the Lines because they all contain a direct pointer to the points.
        for(Point point : points) {
            point.x += dx;
            point.y += dy;
        }
        calculateBounds();
    }
    
    public Polygon copy() {
        Point[] newPoints = new Point[points.length];
        Line[] newLines = new Line[lines.length];
        for(int i = 0; i < newPoints.length; i++) {
            newPoints[i] = new Point(points[i]);
        }
        for(int i = 0; i < newLines.length - 1; i++) {
            newLines[i] = new Line(newPoints[i], newPoints[i + 1]);
        }
        if(newLines.length > 0) {
            newLines[newLines.length - 1] = new Line(newPoints[newPoints.length - 1], newPoints[0]);
        }
        Rectangle newBounds = new Rectangle(bounds);
        return new Polygon(newPoints, newLines, newBounds);
    }
    
    /**
     * Copies the specified Polygon's data into this Polygon.
     * 
     * Only copies the data if the Polygons have equal numbers of points.
     * @param p The Polygon to copy.
     */
    public void copyFrom(Polygon p) {
        //Due to the magic of pointers, this will completely copy everything!!!1!!
        if(p.points.length == points.length) {
            for(int i = 0; i < points.length; i++) {
                points[i].x = p.points[i].x;
                points[i].y = p.points[i].y;
            }
            bounds.x = p.bounds.x;
            bounds.y = p.bounds.y;
            bounds.width = p.bounds.width;
            bounds.height = p.bounds.height;
        }
    }
    
    public Point[] copyPoints() {
        Point[] copy = new Point[points.length];
        System.arraycopy(points, 0, copy, 0, points.length);
        return copy;
    }
}