package alfredo;

import alfredo.input.Keys;
import alfredo.input.Mouse;
import alfredo.paint.Camera;
import alfredo.paint.Canvas;
import alfredo.scene.Scene;
import alfredo.sprite.World;
import alfredo.timing.Interval;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Interval {
    private static final int DEFAULT_DELAY = 33;
    
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 400;
    
    private static final int MIN_WIDTH = 128;
    private static final int MIN_HEIGHT = 64;
    
    private final class GamePanel extends JPanel {
        Canvas canvas;
        
        int bufWidth, bufHeight; //The size that the buffer is drawn at
        int bufX, bufY; //The position that the buffer is drawn at
        
        Mouse mouse;
        
        public GamePanel(int width, int height) {
            this.setSize(width, height);
            this.setPreferredSize(new java.awt.Dimension(width, height));
            
            this.setBackground(new java.awt.Color(0x0));
            
            canvas = new Canvas(width, height);
            
            World.init();
            
            Mouse.Handler adapter = Mouse.init(canvas.getCamera());
            mouse = Mouse.getMouse(adapter);
            addMouseListener(adapter);
            addMouseWheelListener(adapter);
            addMouseMotionListener(adapter);
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            scene.iloop();
            scene.draw(canvas);
            canvas.render(g, bufX, bufY, bufWidth, bufHeight);
            repaint();
        }
        
        
        /**
         * Updates buffer states and mouse factors so that they properly
         * correspond to the size of the window.
         */
        private void updateSize() {
            //Recalculates size and offset of graphical buffer
            if((float)panel.getWidth() / canvas.width > (float)panel.getHeight() / canvas.height) {
                bufHeight = panel.getHeight();
                bufWidth = (int)(bufHeight * ((float)canvas.width / canvas.height));
                bufX = (panel.getWidth() - bufWidth) / 2;
                bufY = 0;
            }
            else {
                bufWidth = panel.getWidth();
                bufHeight = (int)(bufWidth * ((float)canvas.height / canvas.width));
                bufY = (panel.getHeight() - bufHeight) / 2;
                bufX = 0;
            }
            //Update mouse so that coordinates are properly projected
            mouse.updateScreen(bufX + bufWidth / 2, bufY + bufHeight / 2, (double)bufWidth / canvas.width);
        }
    }
    
    private final class GameFrame extends JFrame {
        public GameFrame(String title) {
            super(title);
            
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            addKeyListener(Keys.init());
            
            this.setMinimumSize(new java.awt.Dimension(MIN_WIDTH, MIN_HEIGHT));
            this.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    int width = GameFrame.this.getWidth();
                    int height = GameFrame.this.getHeight();
                    
                    if (width < MIN_WIDTH) { width = MIN_WIDTH; }
                    if (height < MIN_HEIGHT) { height = MIN_HEIGHT; }
                    
                    GameFrame.this.setSize(width, height);
                    
                    panel.updateSize();
                }
            });
        }
    }
    
    public static int getTick() {
        return tick;
    }
    
    GameFrame frame;
    GamePanel panel;
    
    Scene scene;
    
    private static int tick;
    
    private boolean fullscreen = false;
    private Dimension nonFullscreenSize;
    private java.awt.Point nonFullscreenPos;
    
    /**
     * A public handle to the Game object so that it can be properly manipulated.
     * 
     * This object is only instantiated once run() is called.
     */
    public static Game game;
    
    public Game(String title, int width, int height, double scalex, double scaley) {
        super(DEFAULT_DELAY);
        
        
        
        frame = new GameFrame(title);
        panel = new GamePanel(width, height);
        panel.setPreferredSize(new Dimension((int)(width * scalex), (int)(height * scaley)));
        
        frame.add(panel);
        frame.pack();
        
        nonFullscreenSize = new Dimension();
        frame.getSize(nonFullscreenSize);
        
        nonFullscreenPos = new java.awt.Point();
        frame.getLocation(nonFullscreenPos);
        
        scene = Scene.getEmptyScene();
        
        
        tick = 0;
    }
    
    public Game(String title, int width, int height, double scale) {
        this(title, width, height, scale, scale);
    }
    
    public Game(String title, int width, int height) {
        this(title, width, height, 1);
    }
    
    public Game(String title) {
        this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public Game(int width, int height) {
        this("Untitled Game", width, height, 1);
    }
    
    public Game() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public void setup() { }
    
    public final void init() {
        game = this;
    }
    
    public final void run() {
        if(game == null) { game = this; }
        setup();
        frame.setVisible(true);
        this.start();
    }
    
    @Override
    public final void loop() {
        tick++;
        scene.loop();
    }
    
    public final void setScene(Scene scene) {
        this.scene = scene;
        scene.onActivate();
    }
    
    public void setFullscreen(boolean fullscreen) {
        if(fullscreen) {
            frame.getSize(nonFullscreenSize);
            frame.getLocation(nonFullscreenPos);
            frame.dispose(); //Dumb hack to prevent frame from blowing up
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
            frame.setUndecorated(true);
            frame.setVisible(true);
            
            this.fullscreen = true;
        }
        else {
            frame.dispose();
            frame.setExtendedState(JFrame.NORMAL);
            frame.setSize(nonFullscreenSize);
            frame.setLocation(nonFullscreenPos);
            frame.setUndecorated(false);
            frame.setVisible(true);
            
            this.fullscreen = false;
        }
    }
    
    public boolean isFullscreen() { return fullscreen; }
    
    public static Canvas getCanvas() {
        return game.panel.canvas;
    }
    
    //TODO: Robustify camera system
    public static void setCamera(Camera c) {
        game.panel.canvas.setCamera(c);
    }
    
    
}