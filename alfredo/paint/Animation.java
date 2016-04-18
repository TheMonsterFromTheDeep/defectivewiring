package alfredo.paint;

import alfredo.util.Resources;
import java.awt.image.BufferedImage;

public class Animation extends Image {
    static final byte MODE_NONE = 0;
    static final byte MODE_END = 1;
    static final byte MODE_LOOP = 2;
    static final byte MODE_PAUSE = 3;
    
    Image[] frames; //Layer of image for fast drawing stuffz
    
    float frame;
    float step;
    int end;
    int begin;
    
    boolean switchFlag = false;
    
    byte mode;
    
    public static Animation load(String path, int numFrames) {
        return new Animation(Resources.loadImage(path), numFrames);
    }
    
    private void calcFrame() {
        int iframe = (int)frame;
        iframe %= frames.length;
        while(iframe < 0) { iframe += frames.length; }
        image = frames[iframe].image;
    }
    
    public Animation(BufferedImage b, int numFrames) {
        if(b == null) {
            b = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        int frameWidth = b.getWidth() / numFrames;
        frames = new Image[numFrames];
        for(int i = 0; i < numFrames; i++) {
            frames[i] = new Image(b.getSubimage(i * frameWidth, 0, frameWidth, b.getHeight()));
        }
        
        frame = 0;
        image = frames[(int)frame].image;
        
        begin = -1;
        end = -1;
        
        step = 1;
        
        mode = MODE_NONE;
    }
    
    //public void transition(int halt) {
    //    this.end = halt;
    //    isAnimated = true;
    //}
    
    public void transition(int begin, int end) {
        this.end = end;
        frame = begin;
        mode = MODE_END;
    }
    
    public void transition(int begin, int end, float step) {
        transition(begin, end);
        this.step = step;
    }
    
    public void loop(int begin, int end) {
        this.begin = begin;
        this.end = end;
        frame = begin;
        mode = MODE_LOOP;
    }
    
    public void loop(int begin, int end, float step) {
        loop(begin, end);
        this.step = step;
    }
    
    public void pause() {
        mode = MODE_PAUSE;
    }
    
    public void pause(int frame) {
        this.frame = frame;
        mode = MODE_PAUSE;
        calcFrame();
    }
    
    /** Clears all mode data. */
    public void end() {
        mode = MODE_NONE;
    }
    
    
    
    public void step(float amount) {
        if(mode != MODE_PAUSE) {
            frame += amount;
            if((int)frame == end) {
                switchFlag = true;
            }
            if(switchFlag && (int)frame != end) {
                switch(mode) {
                    case MODE_END:
                        frame = end;
                        mode = MODE_PAUSE;
                        break;
                    case MODE_LOOP:
                        frame = begin;
                        break;
                }
                switchFlag = false;
            }
            calcFrame();
        }
    }
    
    /**
     * Steps the default amount. This can be specified by several mode methods.
     */
    public void step() {
        step(step);
    }
    
    public int frame() { return (int)frame; }
    
    public boolean active() { return mode != MODE_PAUSE; }
    
    //TODO: Bounds checking / modulo or something
    public Image getFrame(int frame) {
        return frames[frame];
    }
}