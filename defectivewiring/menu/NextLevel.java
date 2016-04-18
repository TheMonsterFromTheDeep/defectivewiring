package defectivewiring.menu;

import alfredo.Game;
import alfredo.geom.Rectangle;
import alfredo.input.Keys;
import alfredo.input.Mouse;
import alfredo.paint.Canvas;
import alfredo.paint.Image;
import alfredo.paint.StaticCamera;
import alfredo.scene.Scene;
import alfredo.sound.Sound;
import alfredo.util.Resources;
import defectivewiring.Main;
import defectivewiring.Transition;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class NextLevel extends Scene {

    private static NextLevel nextlevel;
    
    public static NextLevel getNextLevel(Game parent, Sound music, int current) {
        if(nextlevel == null) { nextlevel = new NextLevel(parent); }
        nextlevel.music = music;
        nextlevel.current = current;
        return nextlevel;
    }
    
     
    Image bg;
    
    Image previous;
    Image next;
    Image replay;
    Image select;
    
    Image previousCant;
    Image nextCant;
    
    Rectangle previousRect;
    Rectangle nextRect;
    Rectangle replayRect;
    Rectangle selectRect;
    
    static final byte BUTTON_NONE = 0;
    static final byte BUTTON_PREVIOUS = 1;
    static final byte BUTTON_NEXT = 2;
    static final byte BUTTON_REPLAY = 3;
    static final byte BUTTON_SELECT = 4;
    
    byte button = BUTTON_NONE;
    
    Sound sselect;
    Sound choose;
    
    Sound music;
    
    int current;
    
    public NextLevel(Game parent) {
        super(parent);
        
        bg = Image.load("/img/nextlevel/menu.png");
        
        previous = Image.load("/img/nextlevel/previous.png");
        next = Image.load("/img/nextlevel/next.png");
        replay = Image.load("/img/nextlevel/replay.png");
        select = Image.load("/img/nextlevel/select.png");
        
        previousCant = Image.load("/img/nextlevel/previous_cant.png");
        nextCant = Image.load("/img/nextlevel/next_cant.png");
        
        sselect = Resources.loadSound("/snd/select.wav");
        choose = Resources.loadSound("/snd/choose.wav");
        
        //#copypasted from MainMenu
        previousRect = new Rectangle(-53, -12, 112, 14); //#hardcoding (not like difficult coding, but like specifying values arbitrarily)
        nextRect = new Rectangle(-53, 4, 64, 14);
        replayRect = new Rectangle(-53, 20, 88, 14);
        selectRect = new Rectangle(-53, 36, 88, 14);
    }
    
    @Override public void onActivate() { Game.setCamera(new StaticCamera(Game.getCanvas())); }
    
    @Override
    public void draw(Canvas canvas) {
        canvas.draw(bg, -120, -90);
        switch(button) {
            case BUTTON_PREVIOUS:
                canvas.draw(previous, previousRect.x, previousRect.y);
                break;
            case BUTTON_NEXT:
                canvas.draw(next, nextRect.x, nextRect.y);
                break;
            case BUTTON_REPLAY:
                canvas.draw(replay, replayRect.x, replayRect.y);
                break;
            case BUTTON_SELECT:
                canvas.draw(select, selectRect.x, selectRect.y);
                break;
        }
        if(current == 1) {
            canvas.draw(previousCant, previousRect.x, previousRect.y);
        }
        if(current == 6) {
            canvas.draw(nextCant, nextRect.x, nextRect.y);
        }
    }

    @Override
    public void loop() { }
    
    @Override
    public void iloop() {
        byte old = button;
        if(previousRect.contains(Mouse.getPosition()) && current > 1) {
            button = BUTTON_PREVIOUS;
        } else if(nextRect.contains(Mouse.getPosition()) && current < 6) {
            button = BUTTON_NEXT;
        } else if(replayRect.contains(Mouse.getPosition())) {
            button = BUTTON_REPLAY;
        } else if(selectRect.contains(Mouse.getPosition())) {
            button = BUTTON_SELECT;
        } else { button = BUTTON_NONE; }
        if(button != old && button != BUTTON_NONE) {
            sselect.play();
        }
        if(Mouse.isLMBDown()) {
            Main main;
            if(button != BUTTON_NONE) { choose.play(); }
            switch(button) {
                case BUTTON_PREVIOUS:
                    if(current > 1) {
                        main = Main.getMain(parent);
                        main.loadLevel(current - 1);
                        Transition.transition(this, main);
                    }
                    break;
                case BUTTON_NEXT:
                    if(current < 6) {
                        main = Main.getMain(parent);
                        main.loadLevel(current + 1);
                        Transition.transition(this, main);
                    }
                    break;
                case BUTTON_REPLAY:
                    main = Main.getMain(parent);
                    main.loadLevel(current);
                    Transition.transition(this, main);
                    break;
                case BUTTON_SELECT:
                    music.stop();
                    Transition.transition(this, LevelSelect.getLevelSelect(parent));
                    break;
            }
        }
    }
    
}