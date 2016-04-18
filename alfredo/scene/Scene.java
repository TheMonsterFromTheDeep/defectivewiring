package alfredo.scene;

import alfredo.Game;
import alfredo.paint.Canvas;

/**
 * Each Scene is some sort of graphical component of a game with its own graphical
 * and interface logic.
 * @author TheMonsterFromTheDeep
 */
public abstract class Scene {
    public static Scene getEmptyScene() {
        return new Scene(null) { //Null parent does not matter; empty scene will never call any parent methods
            @Override
            public void draw(Canvas canvas) { }

            @Override
            public void loop() { }
        };
    }
    
    public final Game parent;
    
    public Scene(Game parent) {
        this.parent = parent;
    }

    public abstract void draw(Canvas canvas);
    public abstract void loop();
    //Called whenever draw is called and the caller wants paint-based logic to be updated.
    public void iloop() { }
    //canvas is parented to Game
    public void onActivate() { }
}