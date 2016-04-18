package defectivewiring;

import alfredo.Game;
import alfredo.input.Keys;
import alfredo.paint.Canvas;
import alfredo.paint.Image;
import alfredo.paint.StaticCamera;
import alfredo.scene.Scene;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Transitions between stuff.
 * @author 
 */
public class Transition extends Scene {
    
    static final int STEP_SIZE = 8;
    
    Scene first, second;
    
    final Image render;
    final Image firstRender;
    
    int alpha = 0x05;
    
    public static void transition(Scene first, Scene second) {
        first.parent.setScene(new Transition(first, second));
    }
    
    public Transition(Scene first, Scene second) {
        super(first.parent);
        this.first = first;
        this.second = second;
        
        Canvas c = new Canvas(240, 180);
        
        firstRender = new Image();
        render = new Image();
        
        c.setCamera(Game.getCanvas().getCamera());
        first.draw(c);
        firstRender.image = c.getRender();
        
        c = new Canvas(240, 180);
        
        second.onActivate();
        c.setCamera(Game.getCanvas().getCamera());
        second.draw(c);
        render.image = c.getRender();
        
        updateImage();
        
        Game.setCamera(new StaticCamera(Game.getCanvas()));
    }

    @Override
    public void draw(Canvas canvas) {
        synchronized(render) {
            canvas.draw(firstRender, -120, -90);
            canvas.draw(render, -120, -90);
        }
    }
    
    private void updateImage() {
        int ralpha = alpha << 24;
        for(int x = 0; x < render.image.getWidth(); x++) {
            for(int y = 0; y < render.image.getHeight(); y++) {
                render.image.setRGB(x, y, (render.image.getRGB(x, y) & 0x00ffffff) + ralpha);
            }
        }
    }

    @Override
    public void loop() {
        synchronized(render) {
            updateImage();
            alpha += STEP_SIZE;
            if(alpha > 0xff) {
                parent.setScene(second);
            }
        }
    }
}