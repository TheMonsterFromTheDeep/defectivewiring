package alfredo.util;

import alfredo.sound.NullSound;
import alfredo.sound.Sound;
import alfredo.sound.SoundClip;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public final class Resources {
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Resources.class.getResource(path));
        }
        catch (Exception e) {
            System.err.println("Failed to load image resource (" + path + "): " + e.getLocalizedMessage());
            return null;
        }
    }
    
    public static Sound loadSound(String path)
    {
        try {
            Clip clip = AudioSystem.getClip();
            InputStream audioSrc = Resources.class.getClass().getResourceAsStream(path);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            clip.open(audioStream);
            return new SoundClip(clip);        
        } 
        catch (Exception e) { //Catch all exceptions because multicatch *will* fail sometimes :/
            System.err.println("Failed to load audio resource (" + path + "): " + e.getLocalizedMessage());
            return new NullSound(); 
        }
    }
    
    public static String loadString(String path) {
        try {
            InputStream in = Resources.class.getClass().getResourceAsStream(path);
            BufferedReader input = new BufferedReader(new InputStreamReader(in));
            String content = "";
            String line;
            while((line = input.readLine()) != null) {
                content += line;
            }
            return content;
        }
        catch(Exception e) {
             System.err.println("Failed to load string resource (" + path + "): " + e.getLocalizedMessage());
                return "";
        }
    }
}