package alfredo.sound;

/**
 * An empty sound. Used as a fallback for when loading a SoundClip fails.
 * @author TheMonsterFromTheDeep
 */
public class NullSound implements Sound {
    @Override public void play() { }
    @Override public boolean playing() { return false; }
    @Override public void stop() { }
    @Override public void loop() { }
}