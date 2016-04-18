package defectivewiring;

import alfredo.paint.Animation;
import alfredo.paint.Canvas;

public class TextWriter {
    public static TextWriter writer;
    
    static final int FIRST_VALUE = 32;
    static final int CHAR_COUNT = 58;
    
    static public void init() {
        writer = new TextWriter();
    }
    
    public Animation letters;
    
    public TextWriter() {
        letters = Animation.load("/img/text.png", CHAR_COUNT);
    }
    
    public void write(String text, Canvas canvas, float x, float y) {
        for(char c : text.toUpperCase().toCharArray()) {
            canvas.draw(letters.getFrame(c - FIRST_VALUE), x, y);
            x += 6;
        }
    }
}