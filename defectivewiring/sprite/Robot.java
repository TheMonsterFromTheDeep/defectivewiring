package defectivewiring.sprite;

import alfredo.Game;
import alfredo.geom.Point;
import alfredo.geom.Polygon;
import alfredo.paint.Animation;
import alfredo.paint.Image;
import alfredo.sound.Sound;
import alfredo.sprite.Bounds;
import alfredo.sprite.Drawable;
import alfredo.sprite.Entity;
import alfredo.sprite.EntitySet;
import alfredo.sprite.Force;
import alfredo.sprite.Sprite;
import alfredo.util.Resources;
import defectivewiring.Tiles;

public class Robot implements Drawable {
//extends Sprite {
    public Animation appearance;
    
    static final float CENTER_X = 11;
    static final float CENTER_Y = 22.5f;
    
    static final int TRANSFORM_DELAY = 15;
    
    static final int JUMP_HEIGHT_SMALL = 16;
    static final int JUMP_HEIGHT_LARGE = 12;
    
    static final int IDLE_SMALL_BEGIN = 0;
    static final int IDLE_SMALL_END = 5;
    static final int MOVE_LEFT_SMALL_BEGIN = 6;
    static final int MOVE_LEFT_SMALL_END = 7;
    static final int MOVE_RIGHT_SMALL_BEGIN = 8;
    static final int MOVE_RIGHT_SMALL_END = 9;
    static final int TRANSFORM_BEGIN = 10;
    static final int TRANSFORM_END = 16;
    static final int IDLE_LARGE_BEGIN = 17;
    static final int IDLE_LARGE_END = 20;
    static final int MOVE_LEFT_LARGE_BEGIN = 21;
    static final int MOVE_LEFT_LARGE_END = 22;
    static final int MOVE_RIGHT_LARGE_BEGIN = 23;
    static final int MOVE_RIGHT_LARGE_END = 24;
    static final int FALL_SMALL = 25;
    static final int FALL_LARGE = 26;
    
    static final int FRAME_COUNT = 27;
    
    static final float IDLE_STEP = 0.05f;
    
    static final int BULLET_LIFETIME = 50;
    static final int BULLET_VELOCITY = 7;
    static final int BULLET_COOLDOWN = 12;
    
    //public Sprite face;
    ///public Sprite circuit;
    
    public float xVelocity;
    public float xAcceleration;
    
    public float yVelocity;
    public float yAcceleration;
    public boolean gravity = true;
    
    public Bounds activeBounds;
    Force boundsForce;
    
    public final Bounds small;
    public final Bounds large;
    
    int lastTransform;
    
    float maxVel = 5;
    
    public int jump = 0;
    
    public int jumpHeight;
    

    static final byte IDLE = 0;
    static final byte FALL = 1;
    static final byte JUMP = 2;
    static final byte MOVE_LEFT = 3;
    static final byte MOVE_RIGHT = 4;
    static final byte TRANSFORM = 5;
    
    byte status;
    
    public boolean firing = false;
    public boolean firingDirection = false; //False for left; true for right
    
    byte fireCooldown = 0;
    
    public int health = 3;
    
    Sound shoot;
    Sound transform;
    
    private void updateStatus() {
        switch(status) {
                case MOVE_LEFT:
                    if(activeBounds == small) {
                        appearance.loop(MOVE_LEFT_SMALL_BEGIN, MOVE_LEFT_SMALL_END, 1);
                    }
                    else {
                        appearance.loop(MOVE_LEFT_LARGE_BEGIN, MOVE_LEFT_LARGE_END, 1);
                    }
                    break;
                case MOVE_RIGHT:
                    if(activeBounds == small) {
                        appearance.loop(MOVE_RIGHT_SMALL_BEGIN, MOVE_RIGHT_SMALL_END, 1);
                    }
                    else {
                        appearance.loop(MOVE_RIGHT_LARGE_BEGIN, MOVE_RIGHT_LARGE_END, 1);
                    }
                    break;
                case FALL:
                    if(activeBounds == small) {
                        appearance.pause(FALL_SMALL);
                    }
                    else {
                        appearance.pause(FALL_LARGE);
                    }
                    break;
                case JUMP:
                    if(activeBounds == small) {
                        appearance.pause(IDLE_SMALL_BEGIN);
                    }
                    else {
                        appearance.pause(IDLE_LARGE_BEGIN);
                    }
                    break;
                case IDLE:
                    if(activeBounds == small) {
                        appearance.loop(IDLE_SMALL_BEGIN, IDLE_SMALL_END, IDLE_STEP);
                    }
                    else {
                        appearance.loop(IDLE_LARGE_BEGIN, IDLE_LARGE_END, IDLE_STEP);
                    }
                    break;
            }
    }
    
    //hax until i get polygonal raytracing for intersections
    final Polygon getBounds() {
        final float width = 23;
        final float height = 29;
        
        float hWidth = width / 2;
        float hHeight = height / 2;
        
        float qWidth = width / 3;
        float qHeight = height / 3;
        
        Point[] points = new Point[13];
        for(int i = 0; i < 3; i++) {
            points[i] = new Point(-hWidth, qHeight * i - hHeight);
        }
        for(int i = 3; i < 6; i++) {
            points[i] = new Point(qWidth * (i - 3) - hWidth, -hHeight);
        }
        for(int i = 6; i < 9; i++) {
            points[i] = new Point(hWidth, -hHeight + qHeight * (i - 6));
        }
        for(int i = 9; i < 12; i++) {
            points[i] = new Point(hWidth - qWidth * (i - 9), hHeight);
        }
        points[12] = new Point(-hWidth, hHeight);
        return new Polygon(points);
    }
    
    public Robot() {
        //super(Image.load("/img/robot/body_default.png"));
        //face = new Sprite(Image.load("/img/robot/face.png"));
        //face.setParent(this, false);
        //face.setY(1);
        //circuit = new Sprite(Image.load("/img/circuit/circuit_default.png"));
        //circuit.setParent(this, false);
        //circuit.setY(-20);
        
        appearance = Animation.load("/img/robot/robot.png",FRAME_COUNT);
        appearance.pause();
        
        shoot = Resources.loadSound("/snd/robot/shoot.wav");
        transform = Resources.loadSound("/snd/robot/transform.wav");
        
        small = new Bounds(13, 13);
        large = new Bounds(getBounds(), new Point(0, 8)); //I really need to make my game engine better
        //I always find all the problems *after* I start ludum dare
        
        activeBounds = small;
        boundsForce = new Force(activeBounds);
        
        lastTransform = -TRANSFORM_DELAY;
        jumpHeight = JUMP_HEIGHT_SMALL;
        
        status = IDLE;
        updateStatus();
    }
    
    public Force updateX() {
        //moveX(xVelocity);
        if(Math.abs(xVelocity) > maxVel) {
            xAcceleration = 0.3f * ((xVelocity > 0) ? -1 : 1);
            xVelocity += xAcceleration;
        }
        if(Math.abs(xVelocity) < maxVel || (xVelocity < 0) != (xAcceleration < 0)) {
            xVelocity += xAcceleration;
        }
        
        if(xVelocity != 0) { //Retain firing direction even when stopped; nicer for player
            firingDirection = xVelocity > 0; //will be false when left, true when right
        }
        
        //moveY(yVelocity);
        boundsForce.reforce(xVelocity, 0);
        return boundsForce;
        
    }
    
    public Force updateY() {
        yVelocity += yAcceleration;
        
        boundsForce.reforce(0, yVelocity);
        return boundsForce;
    }
    
    public void update() {
        //if(circuit.getY() < -5) { circuit.moveY(1); }
        byte current;
        if(firing) { //Expected to be upheld correctly in Main
            current = firingDirection ? MOVE_RIGHT : MOVE_LEFT;
        }
        else {
            if(xVelocity == 0) {
                if(yVelocity > 0) {
                    current = FALL;
                }
                else if(yVelocity < 0) {
                    current = JUMP;
                }
                else {
                    current = IDLE;
                }
            }
            else {
                if(xVelocity < 0) {
                    current = MOVE_LEFT;
                }
                else {
                    current = MOVE_RIGHT;
                }
            }
        }
       // byte current = (xVelocity < 0) ? MOVE_LEFT : ((xVelocity > 0) ? MOVE_RIGHT : IDLE);
        if(current != status) {
            if(status != TRANSFORM || !appearance.active()) {
                status = current;
                updateStatus();
            }
        }
        
        --fireCooldown;
        
        appearance.step();
    }
    
    public void transform(Tiles t, boolean sound) {
        if(Game.getTick() - lastTransform >= TRANSFORM_DELAY) {
            if(activeBounds == small) {
                int tx = t.getTileX(activeBounds.getWorldX());
                int ty = t.getTileY(activeBounds.getWorldY());
                large.cleanCopyTransform(small);
                
                boolean clear = true;
                
                if(t.checkTile(large, tx + 1, ty)) { clear = false; }
                if(t.checkTile(large, tx - 1, ty)) { clear = false; }
                if(t.checkTile(large, tx - 1, ty - 1)) { clear = false; }
                if(t.checkTile(large, tx, ty - 1)) { clear = false; }
                if(t.checkTile(large, tx + 1, ty - 1)) { clear = false; }
                
                if(clear) { //Actually transform
                    activeBounds = large;
                    boundsForce.reforce(activeBounds, 0, 0);
                    appearance.transition(TRANSFORM_BEGIN, TRANSFORM_END, 1);
                    maxVel = 2.6f;
                    jumpHeight = JUMP_HEIGHT_LARGE;
                    status = TRANSFORM;
                    fireCooldown = 0; //clear cooldown
                    if(sound) { transform.play(); }
                }
            }
            else {
                activeBounds = small;
                activeBounds.cleanCopyTransform(large);
                boundsForce.reforce(activeBounds, 0, 0);
                appearance.transition(TRANSFORM_END, TRANSFORM_BEGIN, -1); //Transform backwards
                maxVel = 5;
                jumpHeight = JUMP_HEIGHT_SMALL;
                status = TRANSFORM;
                if(sound) { transform.play(); }
            }
            lastTransform = Game.getTick();
            
        }
    }
    
    //Expected to only be called when in large form
    public void createBullets(EntitySet<Bullet> bullets) {
        if(fireCooldown <= 0) { 
            float x = activeBounds.getWorldX();
            float y = activeBounds.getWorldY();
            int dir = firingDirection ? 0 : 3; //Offset when bullet is going right rather than left (simply a horizontal shift)
            byte velMul = (byte) (firingDirection ? 1 : -1); //multiplier for velocity
            Bullet first = new Bullet(BULLET_LIFETIME, velMul * BULLET_VELOCITY);
            first.setX(x - 9 + dir);
            first.setY(y - 1.5f);
            Bullet second = new Bullet(BULLET_LIFETIME, velMul * BULLET_VELOCITY);
            second.setX(x + 7 + dir); //the center is off but it's too late to fix it
            second.setY(y - 1.5f);
            bullets.add(first);
            bullets.add(second);
            fireCooldown = BULLET_COOLDOWN;
            shoot.play();
        }
    }

    @Override
    public float getDrawX() {
        return activeBounds.getWorldX() - CENTER_X;
    }

    @Override
    public float getDrawY() {
        return activeBounds.getWorldY() - CENTER_Y;
    }

    @Override
    public float getDrawPivotX() {
       return activeBounds.getWorldX();
    }

    @Override
    public float getDrawPivotY() {
        return activeBounds.getWorldY();
    }

    @Override
    public double getDrawDirection() {
        return activeBounds.getWorldDirection();
    }
}