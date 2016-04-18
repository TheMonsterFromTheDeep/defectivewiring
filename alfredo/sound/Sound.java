package alfredo.sound;

/**
 * A Sound object represents a sound that can be played and stopped.
 * @author TheMonsterFromTheDeep
 */
public interface Sound {
    void play();
    boolean playing();
    void stop();
    void loop();
}
