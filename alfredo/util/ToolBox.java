package alfredo.util;

import alfredo.Game;
import alfredo.input.KeyDownEvent;
import alfredo.input.Keys;
import com.sun.glass.events.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The ToolBox provides simple static functions, for example: making F11 into a fullscreen control.
 * @author TheMonsterFromTheDeep
 */
public class ToolBox {
    public static void addDefaultFullscreen() {
        Keys.watch((KeyDownEvent) (int key) -> {
            if(key == KeyEvent.VK_F11) { Game.game.setFullscreen(!Game.game.isFullscreen()); }
        });
    }
    
    public static void writeFrame(String path) {
        try {
            String number = Integer.toString(Game.getTick());
            String zeroes = "";
            //8 digits is hopefully enough
            for(int i = 0; i < 8 - number.length(); i++) {
                zeroes += '0';
            }
            ImageIO.write(Game.getCanvas().getRender(), "PNG", new File(path + '_' + zeroes + number + ".png"));
        } catch (IOException ex) {
            System.err.println("Couldn't write frame: " + ex.getLocalizedMessage());
        }
    }
}