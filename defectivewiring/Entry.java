package defectivewiring;

import alfredo.Game;
import alfredo.util.ToolBox;
import defectivewiring.menu.MainMenu;

public class Entry extends Game {
    public Entry() {
        super("Defective Wiring", 240, 180, 4); //here are where various dimension paramters go
    }
    
    public static void main(String[] args) {
        //Entry, as in the entry point for the game
        Entry e = new Entry();
        TextWriter.init();
        e.init();
        e.setScene(MainMenu.getMainMenu(e));
        e.setDelay(16);
        ToolBox.addDefaultFullscreen();
        e.run();
    }
}