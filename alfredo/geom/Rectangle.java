package alfredo.geom;

import alfredo.geom.Point;

public class Rectangle {
    public float x;
    public float y;
    public float width;
    public float height;
    
    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public Rectangle(Rectangle r) {
        this.x = r.x;
        this.y = r.y;
        this.width = r.width;
        this.height = r.height;
    }
    
    public Rectangle(Point position, Point size) {
        this.x = position.x;
        this.y = position.y;
        this.width = size.x;
        this.height = size.y;
    }
    
    public float left() { return x; }
    public float top() { return y; }
    public float right() { return x + width; }
    public float bottom() { return y + height; }
    
    public void setLeft(float x) { this.width += (this.x - x); this.x = x; }
    public void setTop(float y) { this.height += (this.y - y); this.y = y; }
    public void setRight(float x) { this.width = (x - this.x); }
    public void setBottom(float y) { this.height = (y - this.y); }
    
    public void shift(float dx, float dy) {
        x += dx;
        y += dy;
    }
    
    public boolean contains(float x, float y) {
        return (x >= this.x && x < this.x + width && y >= this.y && y < this.y + height);
    }
    
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }
    
    public boolean intersects(Rectangle r) {
        float thisx = x;
        float thisy = y;
        float thiswidth = width;
        float thisheight = height;
        float rx = r.x;
        float ry = r.y;
        float rwidth = r.width;
        float rheight = r.height;
        return ((thisx + thiswidth > rx && thisx + thiswidth <= rx + rwidth) || (rx + rwidth > thisx && rx + rwidth <= thisx + thiswidth)) && ((thisy + thisheight > ry && thisy + thisheight <= ry + rheight) || (ry + rheight > thisy && ry + rheight <= thisy + thisheight));
    }
}