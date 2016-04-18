package defectivewiring.menu;

import alfredo.Game;
import alfredo.geom.Rectangle;
import alfredo.input.Mouse;
import alfredo.paint.Canvas;
import alfredo.paint.Image;
import alfredo.paint.StaticCamera;
import alfredo.scene.Scene;
import alfredo.sound.Sound;
import alfredo.util.Resources;
import defectivewiring.Main;
import defectivewiring.Transition;

public class LevelSelect extends Scene {

    private static LevelSelect levelselect;
    
    public static LevelSelect getLevelSelect(Game parent) {
        if(levelselect == null) {
            levelselect = new LevelSelect(parent);
        }
        return levelselect;
    }
    
    Image bg;
    
    Image back;
    Rectangle backRect;
    Image[] numbers;
    Rectangle[] numberRects;
    
    static final byte BUTTON_NONE = -1;
    static final byte BUTTON_BACK = -2;
    
    byte button = BUTTON_NONE;
    
    Sound music;
    
    Sound select;
    Sound choose;
    
    public LevelSelect(Game parent) {
        super(parent);
        
        bg = Image.load("/img/levelselect/menu.png");
        
        back = Image.load("/img/levelselect/back.png");
        
        select = Resources.loadSound("/snd/select.wav");
        choose = Resources.loadSound("/snd/choose.wav");
        
        numbers = new Image[6];
        for(int i = 0; i < numbers.length; i++) {
            numbers[i] = Image.load("/img/levelselect/" + (i + 1) + ".png");
        }
        
        numberRects = new Rectangle[6];
        
        for(int i = 0; i < 3; i++) {
            numberRects[i] = new Rectangle(-98, -24 - (i * 28), 21, 25);
        }
        for(int i = 3; i < 6; i++) {
            numberRects[i] = new Rectangle(78, -24 - ((i - 3) * 28), 21, 25);
        }
        //for(int i = 6; i < 9; i++) {
        //    numberRects[i] = new Rectangle(46, -24 - ((i - 6) * 28), 21, 25);
        //}
        //for(int i = 9; i < 12; i++) {
        //    numberRects[i] = new Rectangle(72, -24 - ((i - 9) * 28), 21, 25);
        //}
        //
        backRect = new Rectangle(-24, 50, 45, 25); //#hardcoding
        //helpRect = new Rectangle(65, -24, 47, 25);
        
        music = MainMenu.getMusic();
        
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.draw(bg, -120, -90);
        if(button == BUTTON_BACK) {
            canvas.draw(back, backRect.x, backRect.y);
        } else if (button > -1) {
            canvas.draw(numbers[button], numberRects[button].x, numberRects[button].y);
        }
    }
    
    @Override public void onActivate() { Game.setCamera(new StaticCamera(Game.getCanvas())); if(!music.playing()) {
            music.loop();
        }}
    
    @Override
    public void loop() { }
    
    @Override
    public void iloop() {
        byte old = button;
        if(backRect.contains(Mouse.getPosition())) {
            button = BUTTON_BACK;
        }
        else {
            boolean contained = false;
            for(int i = 0; i < numberRects.length; i++) {
                if(numberRects[i].contains(Mouse.getPosition())) {
                    contained = true;
                    button = (byte) i;
                }
            }
            if(!contained) { button = BUTTON_NONE; }
        }
        if(button != old && button != BUTTON_NONE) {
            select.play();
        }
        
        if(Mouse.isLMBDown()) {
            if(button != BUTTON_NONE) { choose.play(); }
            if(button == BUTTON_BACK) {
                Transition.transition(this, MainMenu.getMainMenu(parent));
            }
            else if (button > -1) {
                music.stop();
                Main main = Main.getMain(parent);
                main.loadLevel(button + 1); //Worlds are numbered 1, 2, 3, 4...
                Transition.transition(this, main);
            }
        }
    }
    
}