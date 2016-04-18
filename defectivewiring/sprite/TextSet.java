package defectivewiring.sprite;

import alfredo.paint.Canvas;
import alfredo.sprite.Entity;

public class TextSet extends Entity {
    public Text current;
    Text[] texts;
    
    int currentLine = 0;
    int currentChar = 0;
    int currentText = 0;
    
    public boolean complete = true;
    
    int delay = 10;
    
    void reset() {
        currentLine = 0;
        currentChar = 0;
    }
    
    public TextSet(Text[] texts, int tilex, int tiley) {
        this.texts = texts;
        if(texts.length > 0) {
            current = new Text(texts[0].lines.length, tilex, tiley);
        }
        else {
            current = new Text(0, tilex, tiley);
        }
        this.setX(tilex * 16 + 9);
        this.setY(tiley * 16 + 9);
    }
    
    public void update() {
        if(!complete) {
            if(currentText < texts.length) {
                current.lines[currentLine] += texts[currentText].lines[currentLine].charAt(currentChar);
                ++currentChar;
                if(currentChar >= texts[currentText].lines[currentLine].length()) {
                    currentChar = 0;
                    ++currentLine;
                    if(currentLine >= texts[currentText].lines.length) {
                        ++currentText;
                        reset();
                        complete = true;
                        delay = 0;
                    }
                }
            }
            else {
                if(delay == 10) {
                    currentText = 0;
                    reset();
                    complete = true;
                }
            }
            
        }
        if(delay < 10) { ++delay; }
    }
    
    public void begin() {
        if(complete && delay == 10) {
            if(currentText < texts.length) {
                reset();
                current.lines = new String[texts[currentText].lines.length];
                for(int i = 0; i < current.lines.length; i++) {
                    current.lines[i] = "";
                }
                complete = false;
            }
            else {
                reset();
                current.lines = new String[0];
                complete = false;
                delay = 0;
            }
        }
    }
    
    public void finish() {
        if(!complete) {
            if(currentText < texts.length) {
                System.arraycopy(texts[currentText].lines, 0, current.lines, 0, texts[currentText].lines.length);
                complete = true;
            }
            else {
                delay = 10;
                complete = true;
            }
        }
    }
    
    public void render(Canvas c) {
        current.render(c);
    }
}