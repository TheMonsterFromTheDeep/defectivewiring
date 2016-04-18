package alfredo.sound;

import javax.sound.sampled.Clip;

/**
 * Represents a sound that has been loaded from an audio file.
 * @author TheMonsterFromTheDeep
 */
public class SoundClip implements Sound {
    
    Clip clip;
    
    public SoundClip(Clip c) {
        this.clip = c;
    }

    @Override
    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public boolean playing() {
        return clip.isRunning();
    }

    @Override
    public void stop() {
        clip.stop();
    }
    
    /**
     * loops forever
     */
    @Override
    public void loop() {
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
}