package defectivewiring.sprite;

import alfredo.paint.Canvas;
import alfredo.sprite.Entity;
import defectivewiring.TextWriter;

/**
 * Represents some sort of scary message communicated by the guy.
 * @author TheMonsterFromTheDeep
 */
public final class Text extends Entity {
    public String[] lines;
    
    public Text(String data, int tilex, int tiley, int tileWidth) {
        //add one because one character is only width 5
        int charsPerLine = (((tileWidth - 1) * 16) + 1) / 6;
        String[] words = data.split(" ");
        int wordIndex = 0;
        int lineIndex = 0;
        int lineCount = 0;//(int)Math.ceil((float)data.length() / charsPerLine);
        while(wordIndex < words.length) {
            int usedChars = 0;
            while(usedChars < charsPerLine && wordIndex < words.length) {
                if(usedChars + words[wordIndex].length() + (usedChars == 0 ? 0 : 1) <= charsPerLine) {
                    usedChars += words[wordIndex].length() + (usedChars == 0 ? 0 : 1);
                    ++wordIndex;
                }
                else {
                    break;
                }
            }
            ++lineCount;
        }
        wordIndex = 0;
        lines = new String[lineCount];
        while(wordIndex < words.length) {
            int usedChars = 0;
            lines[lineIndex] = "";
            while(usedChars < charsPerLine && wordIndex < words.length) {
                if(usedChars + words[wordIndex].length() + (usedChars == 0 ? 0 : 1) <= charsPerLine) {
                    lines[lineIndex] += (usedChars == 0 ? "" : " ") + words[wordIndex];
                    usedChars += words[wordIndex].length() + (usedChars == 0 ? 0 : 1);
                    ++wordIndex;
                }
                else {
                    break;
                }
            }
            ++lineIndex;
            //lines[i] = data.substring(i * charsPerLine, (i + 1) * charsPerLine);
        }
        //System.out.println("My lines:");
        //for(String s: lines) {
         //   System.out.println(s);
        //}
        
        //lines[lineCount - 1] = data.substring((lineCount - 1) * charsPerLine);
        this.setX(tilex * 16 + 9);
        this.setY(tiley * 16 + 9);
    }
    
    public Text(int lineCount, int tilex, int tiley) {
        lines = new String[lineCount];
        for(int i = 0; i < lineCount; i++) {
            lines[i] = "";
        }
        this.setX(tilex * 16 + 9);
        this.setY(tiley * 16 + 9);
    }
    
    public void render(Canvas c) {
        float x = getWorldX();
        float y = getWorldY();
        for(int i = 0; i < lines.length; i++) {
            TextWriter.writer.write(lines[i], c, x, y + i * 8);
        }
    }
}