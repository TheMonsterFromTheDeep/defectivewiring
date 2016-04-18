package alfredo.input;

import alfredo.Game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * The Keys class provides methods for capturing key events and interacting with them.
 * @author TheMonsterFromTheDeep
 */
public class Keys { 
    public class Handler implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            lastChar = e.getKeyChar();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() < states.length) {
                states[e.getKeyCode()] = true;
            }
            for(KeyUpEvent u : keyUpEvents) {
                u.keyUp(e.getKeyCode());
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() < states.length) {
                states[e.getKeyCode()] = false;
            }
            for(KeyDownEvent u : keyDownEvents) {
                u.keyDown(e.getKeyCode());
            }
        }
    }
    
    private boolean[] states;
    private final Handler handler;
    
    private char lastChar;
    
    private ArrayList<KeyUpEvent> keyUpEvents;
    private ArrayList<KeyDownEvent> keyDownEvents;
    
    private Keys() {
        states = new boolean[256];
        handler = new Handler();
        
        keyUpEvents = new ArrayList();
        keyDownEvents = new ArrayList();
    }
    
    private static Keys keys;
    
    public static Handler init() {
        if(keys == null) {
            keys = new Keys();
            return keys.handler;
        }
        return null;
    }
    
    public static boolean pressed(int keyCode) {
        return keys.states[keyCode];
    }
    
    public static boolean pressed(char keyChar) {
        return keys.states[KeyEvent.getExtendedKeyCodeForChar(keyChar)];
    }
    
    public static char getLastChar() {
        return keys.lastChar;
    }
    
    public static void watch(KeyUpEvent e) {
        keys.keyUpEvents.add(e);
    }
    
    public static void watch(KeyDownEvent e) {
        keys.keyDownEvents.add(e);
    }
}