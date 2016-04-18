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

public class MainMenu extends Scene {

    private static MainMenu mainmenu;
    
    //The MainMenu hosts the Sound clip
    public static Sound getMusic() {
        if(mainmenu.music == null) {
            mainmenu.music = Resources.loadSound("/snd/menumusic.wav");
        }
        return mainmenu.music;
    }
    
    Image bg;
    
    Image play;
    Image help;
    
    Image helpMenu;
    
    Rectangle playRect;
    Rectangle helpRect;
    
    static final byte BUTTON_NONE = 0;
    static final byte BUTTON_PLAY = 1;
    static final byte BUTTON_HELP = 2;
    
    byte button = BUTTON_NONE;
    
    public Sound music;
    
    Sound select;
    Sound choose;
    
    boolean showhelp = false;
    
    public MainMenu(Game parent) {
        super(parent);
        
        bg = Image.load("/img/mainmenu/menu.png");
        
        play = Image.load("/img/mainmenu/play.png");
        help = Image.load("/img/mainmenu/help.png");
        
        helpMenu = Image.load("/img/mainmenu/helpmenu.png");
        
        music = Resources.loadSound("/snd/menumusic.wav");
        select = Resources.loadSound("/snd/select.wav");
        choose = Resources.loadSound("/snd/choose.wav");
        
        playRect = new Rectangle(-111, -24, 48, 25); //#hardcoding (not like difficult coding, but like specifying values arbitrarily)
        helpRect = new Rectangle(65, -24, 47, 25);
        
        music.loop();
    }
    
    @Override public void onActivate() { Game.setCamera(new StaticCamera(Game.getCanvas())); }

    @Override
    public void draw(Canvas canvas) {
        canvas.draw(bg, -120, -90);
        switch(button) {
            case BUTTON_PLAY:
                canvas.draw(play, playRect.x, playRect.y);
                break;
            case BUTTON_HELP:
                canvas.draw(help, helpRect.x, helpRect.y);
                break;
        }
        if(showhelp) {
            canvas.draw(helpMenu, -68, -28);
        }
    }

    @Override
    public void loop() { }
    
    @Override
    public void iloop() {
        byte old = button;
        if(playRect.contains(Mouse.getPosition())) {
            button = BUTTON_PLAY;
        }
        else if(helpRect.contains(Mouse.getPosition())) {
            button = BUTTON_HELP;
        } else { button = BUTTON_NONE; }
        if(button != old && button != BUTTON_NONE) {
            select.play();
        }
        
        if(Mouse.isLMBDown()) {
            showhelp = false;
            if(button != BUTTON_NONE) { choose.play();}
            switch(button) {
                case BUTTON_PLAY:
                    Transition.transition(this, LevelSelect.getLevelSelect(parent));
                    break;
                case BUTTON_HELP:
                    showhelp = true;
                    break;
            }
            
        }
    }
    
    public static MainMenu getMainMenu(Game parent) {
        if(mainmenu == null) {
            mainmenu = new MainMenu(parent);
        }
        return mainmenu;
        
    }
    
}