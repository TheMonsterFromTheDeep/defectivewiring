package defectivewiring;

import alfredo.Game;
import alfredo.geom.Point;
import alfredo.geom.Polygon;
import alfredo.input.Keys;
import alfredo.paint.Animation;
import alfredo.paint.Canvas;
import alfredo.paint.Image;
import alfredo.paint.WorldCamera;
import alfredo.scene.Scene;
import alfredo.sound.Sound;
import alfredo.sprite.Bounds;
import alfredo.sprite.EntityPool;
import alfredo.sprite.EntitySet;
import alfredo.sprite.Force;
import alfredo.sprite.SpriteBatch;
import alfredo.util.Resources;
import alfredo.util.ToolBox;
import defectivewiring.menu.LevelSelect;
import defectivewiring.menu.NextLevel;
import defectivewiring.sprite.Bullet;
import defectivewiring.sprite.Minigun;
import defectivewiring.sprite.Pod;
import defectivewiring.sprite.Rebuilder;
import defectivewiring.sprite.Robot;
import defectivewiring.sprite.Shell;
import defectivewiring.sprite.Text;
import defectivewiring.sprite.TextSet;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * The main Scene in the game. Encapsulates all gameplay.
 * @author TheMonsterFromTheDeep
 */
public class Main extends Scene {

    private static Main main;
    
    public static Main getMain(Game parent) {
        if(main == null) {
            main = new Main(parent);
        }
        return main;
    }
    
    public static final byte TILE_SKY = 0;
    public static final byte TILE_GROUND_BASIC = 1;
    public static final byte TILE_GROUND_LIGHT = 2;
    public static final byte TILE_WALL_BASIC = 3;
    public static final byte TILE_SIDE_PILLAR = 4;
    public static final byte TILE_SIDE_PILLAR_BOTTOM = 5;
    public static final byte TILE_WALL_TOP = 6;
    public static final byte TILE_WALL_LEFT = 7;
    public static final byte TILE_WALL_RIGHT = 8;
    public static final byte TILE_WALL_BOTTOM = 9;
    public static final byte TILE_WALL_TL = 10;
    public static final byte TILE_WALL_TR = 11;
    public static final byte TILE_WALL_BL = 12;
    public static final byte TILE_WALL_BR = 13;
    public static final byte TILE_WALL_LIGHT = 14;
    public static final byte TILE_SIDE_PILLAR_CRUMBLE = 15;
    public static final byte TILE_WALL_DISP = 16;
    public static final byte TILE_WALL_DISP_TL = 17;
    public static final byte TILE_WALL_DISP_TR = 18;
    public static final byte TILE_WALL_DISP_BR = 19;
    public static final byte TILE_WALL_DISP_BL = 20;
    public static final byte TILE_WALL_DISP_TOP = 21;
    public static final byte TILE_WALL_DISP_LEFT = 22;
    public static final byte TILE_WALL_DISP_BOTTOM = 23;
    public static final byte TILE_WALL_DISP_RIGHT = 24;
    public static final byte TILE_WALL_GUY = 25;
    public static final byte TILE_WALL_DARK = 26;
    public static final byte TILE_WALL_ELEVATOR_TL = 27;
    public static final byte TILE_WALL_ELEVATOR_TR = 28;
    public static final byte TILE_WALL_ELEVATOR_BL = 29;
    public static final byte TILE_WALL_ELEVATOR_BR = 30;
    public static final byte TILE_WALL_DARK_VENT = 31;
    public static final byte TILE_ROCK = 32;
    public static final byte TILE_SIDE_DARK = 33;
    public static final byte TILE_GROUND_DARK = 34;
    public static final byte TILE_SIDE_DARK_CRUMBLE = 35;
    public static final byte TILE_REBUILDER = 36;
    
    public static final byte IMG_COUNT = 37;
    
    //static final int TILE_OFFSETX = -135;
    //static final int TILE_OFFSETY = -110;
    
    final EntityPool pool; //Everyone loves locking!!!
    
    //Image bg;
    
    Image[] tile;
    
    Robot robot;
    Sound roboJump;
    Sound roboDestroy;
    
    Sound explode;
    
    EntitySet<Bullet> bullets;
    EntitySet<Minigun> miniguns;
    EntitySet<Pod> pods;
    EntitySet<Shell> shells;
    EntitySet<TextSet> signs;
    EntitySet<Rebuilder> rebuilders;
    
    SpriteBatch batch;
    
    WorldCamera camera;
    
    Tiles tiles;
    
    Bounds[] tileBounds;
    //Bounds tileBounds;
    
    Bounds exit;
    
    Animation bullet;
    Animation minigun;
    Image pod;
    
    Animation shell;
    
    Image health;
    
    Sound music;
    
    Sound shoot;
    
    Point checkpoint;
    
    boolean animswitch = false;
    
    final TextSet emptyText;
    TextSet currentTextSet;
    float currentTextDist = 0;
    
    final Bounds square, sky;
    
    int level = 1;
    
    private static boolean isWall(byte b) {
        return b == TILE_WALL_BASIC || b == TILE_WALL_LIGHT || b == TILE_SIDE_PILLAR_BOTTOM || (b >= TILE_WALL_TOP && b <= TILE_WALL_BR) || (b >= TILE_WALL_DISP && b <= TILE_WALL_GUY) || (b >= TILE_WALL_ELEVATOR_TL && b <= TILE_WALL_ELEVATOR_BR) || b == TILE_ROCK || b == TILE_REBUILDER;
    }
    
    private static boolean isDisp(byte b) {
        return (b >= TILE_WALL_DISP && b <= TILE_WALL_DISP_RIGHT);
    }
    
    private static void doWall(int x, int y, byte[][] world) {
        if(world[x][y] == TILE_WALL_BASIC) {
            if(!isWall(world[x][y-1])) {
                if(!isWall(world[x - 1][y])) {
                    world[x][y] = TILE_WALL_TL;
                }
                else if(!isWall(world[x + 1][y])) {
                    world[x][y] = TILE_WALL_TR;
                }
                else {
                    world[x][y] = TILE_WALL_TOP;
                }
            }
            else if(!isWall(world[x][y + 1])) {
                if(!isWall(world[x - 1][y])) {
                    world[x][y] = TILE_WALL_BL;
                }
                else if(!isWall(world[x + 1][y])) {
                    world[x][y] = TILE_WALL_BR;
                }
                else {
                    world[x][y] = TILE_WALL_BOTTOM;
                }
            }
            else if(!isWall(world[x - 1][y])) {
                world[x][y] = TILE_WALL_LEFT;
            }
            else if(!isWall(world[x + 1][y])) {
                world[x][y] = TILE_WALL_RIGHT;
            }
        }
    }
    
    private static void doDisp(int x, int y, byte[][] world) {
        if(world[x][y] == TILE_WALL_DISP) {
            if(!isDisp(world[x][y - 1])) {
                if(!isDisp(world[x - 1][y])) {
                    world[x][y] = TILE_WALL_DISP_TL;
                }
                else if(!isDisp(world[x + 1][y])) {
                    world[x][y] = TILE_WALL_DISP_TR;
                }
                else {
                    world[x][y] = TILE_WALL_DISP_TOP;
                }
            }
            else if(!isDisp(world[x][y + 1])) {
                if(!isDisp(world[x - 1][y])) {
                    world[x][y] = TILE_WALL_DISP_BL;
                }
                else if(!isDisp(world[x + 1][y])) {
                    world[x][y] = TILE_WALL_DISP_BR;
                }
                else {
                    world[x][y] = TILE_WALL_DISP_BOTTOM;
                }
            }
            else if(!isDisp(world[x - 1][y])) {
                world[x][y] = TILE_WALL_DISP_LEFT;
            }
            else if(!isDisp(world[x + 1][y])) {
                world[x][y] = TILE_WALL_DISP_RIGHT;
            }
        }
    }
    
    private static void doWalls(byte[][] world) {
        for(int x = 1; x < world.length - 1; x++) {
            for(int y = 1; y < world[0].length - 1; y++) {
                doWall(x, y, world);
                doDisp(x, y, world);
            }
        }
    }
    
    final static byte[][] loadWorld(BufferedImage b) {
        byte[][] world = new byte[b.getWidth()][b.getHeight()];
        for(int x = 0; x < world.length; x++) {
            for(int y = 0; y < world[0].length; y++) {
                if(b.getRGB(x, y) == 0xff000000) { world[x][y] = TILE_GROUND_BASIC; }
                if(b.getRGB(x, y) == 0xff767676) { world[x][y] = TILE_GROUND_LIGHT; }
                if(b.getRGB(x, y) == 0xffeeeeee) { world[x][y] = TILE_WALL_BASIC; }
                if(b.getRGB(x, y) == 0xffff0000) { world[x][y] = TILE_SIDE_PILLAR; }
                if(b.getRGB(x, y) == 0xffff7676) { world[x][y] = TILE_SIDE_PILLAR_BOTTOM; }
                if(b.getRGB(x, y) == 0xffe6e6e6) { world[x][y] = TILE_WALL_LIGHT; }
                if(b.getRGB(x, y) == 0xffffba00) { world[x][y] = TILE_SIDE_PILLAR_CRUMBLE; }
                if(b.getRGB(x, y) == 0xffb8b8ff) { world[x][y] = TILE_WALL_DISP; }
                if(b.getRGB(x, y) == 0xff7676ff) { world[x][y] = TILE_WALL_GUY; }
                if(b.getRGB(x, y) == 0xff3f3f3f) { world[x][y] = TILE_WALL_DARK; }
                if(b.getRGB(x, y) == 0xff00ff00) {
                    world[x][y] = TILE_WALL_ELEVATOR_BR;
                    world[x - 1][y] = TILE_WALL_ELEVATOR_BL;
                    world[x][y - 1] = TILE_WALL_ELEVATOR_TR;
                    world[x - 1][y - 1] = TILE_WALL_ELEVATOR_TL;
                }
                if(b.getRGB(x, y) == 0xff363636) { world[x][y] = TILE_WALL_DARK_VENT; }
                if(b.getRGB(x, y) == 0xff004f4f) { world[x][y] = TILE_ROCK; }
                if(b.getRGB(x, y) == 0xffaa0000) { world[x][y] = TILE_SIDE_DARK; }
                if(b.getRGB(x, y) == 0xff2e082e) { world[x][y] = TILE_GROUND_DARK; }
                if(b.getRGB(x, y) == 0xffaa6400) { world[x][y] = TILE_SIDE_DARK_CRUMBLE; }
                if(b.getRGB(x, y) == 0xff62f9ff) { world[x][y] = TILE_REBUILDER; }
            }
        }
        doWalls(world);
        return world;
    }
    
    public void cleanse() {
        bullets.removeAll();
        miniguns.removeAll();
        pods.removeAll();
        shells.removeAll();
        signs.removeAll();
    }
    
    public TextSet loadTextSet(int number, int tilex, int tiley, int tilewidth) {
        String s = Resources.loadString("/img/level/sign_" + number + ".txt");
        String[] lines = s.split("\\|");
        Text[] t = new Text[lines.length];
        for(int i = 0; i < lines.length; i++) {
            t[i] = new Text(lines[i], tilex, tiley, tilewidth);
        }
        return new TextSet(t, tilex, tiley);
    }
    
    public void loadEntity(BufferedImage b) {
        for(int x = 0; x < b.getWidth(); x++) {
            for(int y = 0; y < b.getHeight(); y++) {
                if((b.getRGB(x, y) & 0x00ff0000) == 0x00ff0000) {
                    int dx = x;
                    while(b.getRGB(dx, y) != 0xff000000) { ++dx; }
                    signs.add(loadTextSet((b.getRGB(x, y) & 0x000000ff), tiles.getTileByRawX(x), tiles.getTileByRawY(y), dx - x + 1));
                }
                if(b.getRGB(x, y) == 0xff00ff00) {
                    robot.activeBounds.setX(tiles.getTileByRawX(x) * 16);
                    robot.activeBounds.setY(tiles.getTileByRawY(y) * 16 + 8);
                    checkpoint.copyFrom(robot.activeBounds.getLocation());
                    camera.track(robot.activeBounds);
                }
                if(b.getRGB(x, y) == 0xff0000ff) {
                    Pod p = new Pod();
                    p.setX(tiles.getTileByRawX(x) * 16 + 8);
                    p.setY(tiles.getTileByRawY(y) * 16 + 8);
                    pods.add(p);
                    p.sync();
                }
                if(b.getRGB(x, y) == 0xffab1c00) {
                    exit.setX(tiles.getTileByRawX(x) * 16);
                    exit.setY(tiles.getTileByRawY(y) * 16);
                }
                if(b.getRGB(x, y) == 0xff62f9ff) {
                    rebuilders.add(new Rebuilder(tiles.getTileByRawX(x) * 16 + 8, tiles.getTileByRawY(y) * 16 + 8));
                }
                if(b.getRGB(x, y) == 0xff0090ff) {
                    Minigun m = new Minigun();
                    m.setX(tiles.getTileByRawX(x) * 16);
                    m.setY(tiles.getTileByRawY(y) * 16);
                    miniguns.add(m);
                }
            }
        }
    }
    
    public Main(Game parent) {
        super(parent);
        
        pool = new EntityPool();
        batch = new SpriteBatch();
        
        
        
        tileBounds = new Bounds[IMG_COUNT];
        square = new Bounds(new Polygon(new Point[]{ new Point(0, 0), new Point(0, 16), new Point(16, 16), new Point(16, 0) }));
        sky = new Bounds(new Polygon(new Point[] { }));
        tileBounds[TILE_SKY] = sky;
        tileBounds[TILE_GROUND_BASIC] = square;
        tileBounds[TILE_GROUND_LIGHT] = square;
        tileBounds[TILE_WALL_BASIC] = sky;
        tileBounds[TILE_SIDE_PILLAR] = square;
        tileBounds[TILE_SIDE_PILLAR_BOTTOM] = square;
        tileBounds[TILE_WALL_TOP] = sky;
        tileBounds[TILE_WALL_LEFT] = sky;
        tileBounds[TILE_WALL_RIGHT] = sky;
        tileBounds[TILE_WALL_BOTTOM] = sky;
        tileBounds[TILE_WALL_TL] = sky;
        tileBounds[TILE_WALL_TR] = sky;
        tileBounds[TILE_WALL_BL] = sky;
        tileBounds[TILE_WALL_BR] = sky;
        tileBounds[TILE_WALL_LIGHT] = sky;
        tileBounds[TILE_SIDE_PILLAR_CRUMBLE] = square;
        tileBounds[TILE_WALL_DISP_TOP] = sky;
        tileBounds[TILE_WALL_DISP_LEFT] = sky;
        tileBounds[TILE_WALL_DISP_RIGHT] = sky;
        tileBounds[TILE_WALL_DISP_BOTTOM] = sky;
        tileBounds[TILE_WALL_DISP_TL] = sky;
        tileBounds[TILE_WALL_DISP_TR] = sky;
        tileBounds[TILE_WALL_DISP_BL] = sky;
        tileBounds[TILE_WALL_DISP_BR] = sky;
        tileBounds[TILE_WALL_DISP] = sky;
        tileBounds[TILE_WALL_GUY] = sky;
        tileBounds[TILE_WALL_DARK] = sky;
        tileBounds[TILE_WALL_ELEVATOR_TL] = sky;
        tileBounds[TILE_WALL_ELEVATOR_TR] = sky;
        tileBounds[TILE_WALL_ELEVATOR_BL] = sky;
        tileBounds[TILE_WALL_ELEVATOR_BR] = sky;
        tileBounds[TILE_WALL_DARK_VENT] = sky;
        tileBounds[TILE_ROCK] = square;
        tileBounds[TILE_GROUND_DARK] = square;
        tileBounds[TILE_SIDE_DARK] = square;
        tileBounds[TILE_SIDE_DARK_CRUMBLE] = square;
        tileBounds[TILE_REBUILDER] = sky;
        
        tiles = new Tiles(loadWorld(Resources.loadImage("/img/level/1.png")), tileBounds);
        
        //bg = Image.load("/img/world/bg.png");
        
        tile = new Image[IMG_COUNT];
        tile[TILE_SKY] = Image.load("/img/world/bg.png");
        tile[TILE_GROUND_BASIC] = Image.load("/img/world/ground_basic.png");
        tile[TILE_GROUND_LIGHT] = Image.load("/img/world/ground_basic_light.png");
        tile[TILE_WALL_BASIC] = Image.load("/img/world/wall_basic.png");
        tile[TILE_SIDE_PILLAR] = Image.load("/img/world/side_pillar.png");
        tile[TILE_SIDE_PILLAR_BOTTOM] = Image.load("/img/world/side_pillar_bottom.png"); 
        tile[TILE_WALL_TOP] = Image.load("/img/world/wall_basic_top.png");
        tile[TILE_WALL_LEFT] = Image.load("/img/world/wall_basic_left.png");
        tile[TILE_WALL_RIGHT] = Image.load("/img/world/wall_basic_right.png");
        tile[TILE_WALL_BOTTOM] = Image.load("/img/world/wall_basic_bottom.png");
        tile[TILE_WALL_TL] = Image.load("/img/world/wall_basic_tl.png");
        tile[TILE_WALL_TR] = Image.load("/img/world/wall_basic_tr.png");
        tile[TILE_WALL_BL] = Image.load("/img/world/wall_basic_bl.png");
        tile[TILE_WALL_BR] = Image.load("/img/world/wall_basic_br.png");
        tile[TILE_WALL_LIGHT] = Image.load("/img/world/wall_basic_light.png");
        tile[TILE_SIDE_PILLAR_CRUMBLE] = Image.load("/img/world/side_pillar_crumbling.png");
        tile[TILE_WALL_DISP] = Image.load("/img/world/wall_disp.png");
        tile[TILE_WALL_DISP_TOP] = Image.load("/img/world/wall_disp_top.png");
        tile[TILE_WALL_DISP_LEFT] = Image.load("/img/world/wall_disp_left.png");
        tile[TILE_WALL_DISP_RIGHT] = Image.load("/img/world/wall_disp_right.png");
        tile[TILE_WALL_DISP_BOTTOM] = Image.load("/img/world/wall_disp_bottom.png");
        tile[TILE_WALL_DISP_TL] = Image.load("/img/world/wall_disp_tl.png");
        tile[TILE_WALL_DISP_TR] = Image.load("/img/world/wall_disp_tr.png");
        tile[TILE_WALL_DISP_BL] = Image.load("/img/world/wall_disp_bl.png");
        tile[TILE_WALL_DISP_BR] = Image.load("/img/world/wall_disp_br.png");
        tile[TILE_WALL_GUY] = Image.load("/img/world/wall_guy.png");
        tile[TILE_WALL_DARK] = Image.load("/img/world/wall_dark.png");
        tile[TILE_WALL_ELEVATOR_TL] = Image.load("/img/world/wall_elevator_tl.png");
        tile[TILE_WALL_ELEVATOR_TR] = Image.load("/img/world/wall_elevator_tr.png");
        tile[TILE_WALL_ELEVATOR_BL] = Image.load("/img/world/wall_elevator_bl.png");
        tile[TILE_WALL_ELEVATOR_BR] = Image.load("/img/world/wall_elevator_br.png");
        tile[TILE_WALL_DARK_VENT] = Image.load("/img/world/wall_dark_vent.png");
        tile[TILE_ROCK] = Image.load("/img/world/rock.png");
        tile[TILE_GROUND_DARK] = Image.load("/img/world/ground_dark.png");
        tile[TILE_SIDE_DARK] = Image.load("/img/world/side_dark.png");
        tile[TILE_SIDE_DARK_CRUMBLE] = Image.load("/img/world/side_dark_crumble.png");
        tile[TILE_REBUILDER] = Image.load("/img/world/rebuilder.png");
        
        bullet = Animation.load("/img/weapon/bullet.png",5);
        minigun = Animation.load("/img/enemy/minigun.png",4);
        shell = Animation.load("/img/weapon/shell.png",2);
        pod = Image.load("/img/enemy/pod.png");
        //test.transition(6);
        
        robot = new Robot();
        health = Image.load("/img/health.png");
        checkpoint = new Point();
        //pool.add(robot);
        //pool.add(robot.face);
        //pool.add(robot.circuit);
        
        exit = new Bounds(32, 32);
        
        bullets = new EntitySet(pool);
        miniguns = new EntitySet(pool);
        shells = new EntitySet(pool);
        pods = new EntitySet(pool);
        signs = new EntitySet(pool);
        rebuilders = new EntitySet(pool);
        
        //Text t1 = new Text("BLAH BLAH BLAH", 4, -10, 6);
        //Text t2 = new Text("HOO LA LA", 4, -10, 6);
        
        //signs.add();
        //signs.add();
        
        emptyText = new TextSet(new Text[] { }, 0, 0);
        currentTextSet = emptyText;
        
        //signs.add(new TextSet(new Text[] { t1, t2}, 4, -10));
        
        roboJump = Resources.loadSound("/snd/robot/jump.wav");
        roboDestroy = Resources.loadSound("/snd/robot/destroy.wav");
        explode = Resources.loadSound("/snd/explode.wav");
        music = Resources.loadSound("/snd/music.wav");
        shoot = Resources.loadSound("/snd/shoot.wav");
    }
    
    @Override
    public void onActivate() {
        camera = new WorldCamera(Game.getCanvas());
        Game.setCamera(camera);
        camera.track(robot.activeBounds);
        if(!music.playing()) {
            music.loop();
        }
    }
    
    //TODO: Implement
    public void loadLevel(int number) {
        cleanse();
        if(camera == null) { camera = new WorldCamera(Game.getCanvas()); }
        tiles.data = loadWorld(Resources.loadImage("/img/level/" + number + ".png"));
        tiles.init();
        loadEntity(Resources.loadImage("/img/level/" + number + "_entity.png"));
        level = number;
        robot.health = 3;
    }

    @Override
    public void draw(Canvas canvas) {
        synchronized(pool) {
            batch.begin(canvas);
            
            //float x = robot.getWorldX();
            //float y = robot.getWorldY();
            
            int sx = tiles.getTileX(robot.activeBounds.getWorldX()) - 8;//tiles.getTileX(robot.getWorldX() + TILE_OFFSETX);
            int sy = tiles.getTileY(robot.activeBounds.getWorldY()) - 6;//tiles.getTileY(robot.getWorldY() + TILE_OFFSETY);
            
            
            
            //System.out.println("Tile x, y: " + sx + " " + sy);
            
            //int sx = (int)((x - 140) / 16) * 16;
            //int sy = (int)((y - 110) / 16) * 16;
            
            for(int dx = sx; dx < sx + 17; ++dx) { //need to get into habit of preincrement
                for(int dy = sy; dy < sy + 14; ++dy) {
                    canvas.draw(tile[tiles.getTileByTile(dx, dy)], dx * 16, dy * 16);
                }
            }
            
            //for(Text t : signs) {
            //    t.render(canvas);
            //}
            
            currentTextSet.render(canvas);
            
            batch.draw(robot.appearance, robot);
            
            
            
            for(Minigun m : miniguns) {
                batch.draw(minigun.getFrame(m.frame), m);
            }
            
            for(Pod p : pods) {
                batch.draw(pod, p);
            }
            
            for(Shell s : shells) {
                batch.draw(shell.getFrame(s.frame), s);
            }
            
            for(Bullet b : bullets) {
                batch.draw(bullet.getFrame(b.currentFrame), b);
            }
            
            //batch.ink(robot);
            //batch.draw(robot.circuit);
            //batch.draw(robot.face);
            
            //batch.ink(robot.activeBounds, Color.BLUE);
            
            //batch.ink(robot.large);
            
            
            
            //canvas.draw(test, 0, 0);
            //int tiley = tiles.getTileY(robot.activeBounds.getWorldY() + robot.yVelocity);
            //int robotx = tiles.getTileX(robot.activeBounds.getWorldX());
            //int roboty = tiles.getTileY(robot.activeBounds.getWorldY());
            /*tileBounds[1].setX(tiles.getWorldX(robotx));
            tileBounds[1].setY(tiles.getWorldY(roboty));
            batch.ink(tileBounds[1], Color.RED);
            tileBounds[1].setY(tiles.getWorldY(tiley));
            batch.ink(tileBounds[1]);
            tileBounds[1].moveX(-16);
            batch.ink(tileBounds[1]);
            tileBounds[1].moveX(32);
            batch.ink(tileBounds[1]);*/
            /*int tiley = tiles.getTileY(robot.activeBounds.getWorldY() + robot.yVelocity);
            int deltay = robot.yVelocity > 0 ? 1 : -1;
            for(int cy = roboty; cy - (tiley + deltay) != 0; cy += deltay) {
                tileBounds[1].setX(tiles.getWorldX(robotx - 1));
                tileBounds[1].setY(tiles.getWorldY(cy));
                batch.ink(tileBounds[1]);
                tileBounds[1].moveX(16);
                batch.ink(tileBounds[1]);
                tileBounds[1].moveX(16);
                batch.ink(tileBounds[1]);
            }*/
            
            /*if(robot.yVelocity > 0) {
                for(int cy = roboty; cy <= tiley + 2; ++cy) {
                    tileBounds[1].setX(tiles.getWorldX(robotx - 1));
                    tileBounds[1].setY(tiles.getWorldY(cy));
                    batch.ink(tileBounds[1]);
                    tileBounds[1].moveX(16);
                    batch.ink(tileBounds[1]);
                    tileBounds[1].moveX(16);
                    batch.ink(tileBounds[1]);
                }
            }*/
            for(int i = 0; i < robot.health; i++) {
                canvas.draw(health, camera.location.x - 119 + 9 * i, camera.location.y - 89);
            }
        }
    }
    
    void updateMinigun(Minigun m) {       
        int mx = tiles.getTileX(m.getWorldX());
        int my = tiles.getTileY(m.getWorldY());
        int nmy = tiles.getTileY(m.getWorldY() + m.yVelocity);
        boolean mg = true;
        m.force.reforce(0, m.yVelocity);
        for(int cy = my; cy <= nmy + 1; ++cy) {
            if(tiles.checkTile(m.force, mx, cy)
                    || tiles.checkTile(m.force, mx + 1, cy)
                    || tiles.checkTile(m.force, mx - 1, cy)) {
                m.yVelocity = 0;
                mg = false;
                break;
            }
        }
        if(mg) {
            m.yAcceleration = 0.3f;
        } else {
            m.yAcceleration = 0;
        }
        m.yVelocity += m.yAcceleration;
        m.force.apply();

        m.force.reforce(m.xVelocity, 0);

        if(tiles.checkTile(m.force, mx + 1, my - 1)
        || tiles.checkTile(m.force, mx + 1, my)
        || tiles.checkTile(m.force, mx + 1, my + 1)
        || tiles.checkTile(m.force, mx - 1, my - 1)
        || tiles.checkTile(m.force, mx - 1, my)
        || tiles.checkTile(m.force, mx - 1, my + 1)) {
            m.xVelocity *= -1; //Go opposite direction
            m.frame = m.xVelocity > 0 ? 0 : 2; //Switch graphical direction
        }

        m.force.apply();

        ++m.frame; //Animated frames
        if(m.xVelocity > 0) {
            if(m.frame > 1) { m.frame = 0; }
        }
        else if(m.frame > 3) {
            m.frame = 2;
        }

        if(m.cooldown == 0) {
            //If pointing in direction of player,
            if((robot.activeBounds.getWorldX() - m.getWorldX() > 0) == (m.xVelocity > 0)) {
                //and within range,
                if(Math.abs(m.getWorldY() - robot.activeBounds.getWorldY()) < 10 && Math.abs(m.getWorldX() - robot.activeBounds.getWorldX()) < 96) {
                    byte velMul = (byte) (m.xVelocity > 0 ? 1 : -1);
                    float sx = m.getWorldX() + 5;
                    float sy = m.getWorldY() - 11;
                    Shell first = new Shell(Shell.VELOCITY_X * velMul, 0);
                    first.setX(sx);
                    first.setY(sy);
                    Shell second = new Shell(Shell.VELOCITY_X * velMul, 0.3f);
                    second.setX(sx);
                    second.setY(sy);
                    Shell third = new Shell(Shell.VELOCITY_X * velMul, 0.6f);
                    third.setX(sx);
                    third.setY(sy);
                    shells.add(first); //Add three shells of varying velocities
                    shells.add(second);
                    shells.add(third);
                    m.cooldown = Minigun.FIRE_COOLDOWN;
                    shoot.play();
                }
            }
        }
        else { --m.cooldown; }
    }
    
    void updatePod(Pod p) {
        if(p.cooldown == 0) {
            if(Math.abs(p.getWorldX() - robot.activeBounds.getWorldX()) < 96) {
                float x = p.getWorldX();
                float y = p.getWorldY();
                Shell left = new Shell(-Shell.VELOCITY_X, 0);
                left.setX(x - 7);
                left.setY(y + 1);
                Shell right = new Shell(Shell.VELOCITY_X, 0);
                right.setX(x + 7);
                right.setY(y + 1);
                shells.add(left);
                shells.add(right);
                p.cooldown = Pod.FIRE_COOLDOWN;
                shoot.play();
            }
        }
        else { --p.cooldown; }
    }

    @Override
    public void loop() {
        synchronized(pool) {
            if(robot.activeBounds.touches(exit)) {
                Transition.transition(this, NextLevel.getNextLevel(parent, music, level));
            }
            
            int robotx = tiles.getTileX(robot.activeBounds.getWorldX());
            int roboty = tiles.getTileY(robot.activeBounds.getWorldY());
            
            
            Force f = robot.updateX();
            tiles.checkTile(f, robotx + 1, roboty - 1);
            tiles.checkTile(f, robotx + 1, roboty);
            tiles.checkTile(f, robotx + 1, roboty + 1);
            tiles.checkTile(f, robotx - 1, roboty - 1);
            tiles.checkTile(f, robotx - 1, roboty);
            tiles.checkTile(f, robotx - 1, roboty + 1);
            f.apply();
            robot.updateY();
            boolean gravity = true;
            int tiley = tiles.getTileY(robot.activeBounds.getWorldY() + robot.yVelocity);
            if(robot.yVelocity > 0) {
                for(int cy = roboty; cy <= tiley + 1; ++cy) {
                    if(tiles.checkTile(f, robotx, cy)
                            || tiles.checkTile(f, robotx + 1, cy)
                            || tiles.checkTile(f, robotx - 1, cy)) {
                        robot.yVelocity = 0;
                        gravity = false;
                        break;
                    }
                }
            }
            else {
                for(int cy = roboty; cy >= tiley - 1; --cy) {
                    if(tiles.checkTile(f, robotx, cy)
                            || tiles.checkTile(f, robotx + 1, cy)
                            || tiles.checkTile(f, robotx - 1, cy)) {
                        robot.yVelocity = 0;
                        break;
                    }
                }
            }
            f.apply();
            if(robot.jump > 0) {
                --robot.jump;
                robot.yAcceleration = -0.3f;
            }
            else {
                robot.yAcceleration = gravity ? 0.3f : 0;
            }
            
            for(Rebuilder b : rebuilders) {
                if(robot.activeBounds.touches(b)) {
                    checkpoint.copyFrom(b.getLocation());
                }
            }
            
//            if(Keys.pressed(KeyEvent.VK_SPACE)) {
//                robot.activeBounds.moveX(-0.3f);
//            }
//            if(Keys.pressed(KeyEvent.VK_A)) {
//                robot.activeBounds.moveX(0.3f);
//            }
            
            robot.update();
            camera.track(robot.activeBounds);
            
            if(robot.firing) {
                robot.createBullets(bullets);
            }
            
            for(Shell s : shells) {
                --s.lifetime;
                if(s.lifetime == 0) { shells.removeCurrent(); }
                if(robot.activeBounds.bounds.contains(s.getLocation())) {
                    robot.health -= (robot.activeBounds == robot.small) ? 2 : 1; //Lose more health when small
                    shells.removeCurrent();
                    explode.play();
                }
                
                s.moveX(s.velX);
                s.moveY(s.velY);
            }
            
            for(Bullet b : bullets) {
                b.update();
                int tileX = tiles.getTileX(b.getWorldX());
                int tileY = tiles.getTileY(b.getWorldY());
                switch(tiles.getTileByTile(tileX, tileY)) {
                    case TILE_SIDE_PILLAR_CRUMBLE:
                        tiles.setTile(tileX, tileY, TILE_WALL_BASIC);
                        break;
                    case TILE_SIDE_DARK_CRUMBLE:
                        tiles.setTile(tileX, tileY, TILE_WALL_DARK);
                        break;
                }
                if(b.lifetime == 0 || tileBounds[tiles.getTileByTile(tileX, tileY)] == square) {
                    bullets.removeCurrent();
                }
                
                for(Minigun m : miniguns) {
                    if(m.bounds.contains(b.getLocation())) {
                        miniguns.removeCurrent();
                        bullets.removeCurrent();
                        explode.play();
                    }
                }
                
                for(Pod p : pods) {
                    if(p.bounds.contains(b.getLocation())) {
                        pods.removeCurrent();
                        bullets.removeCurrent();
                        explode.play();
                    }
                }
            }
            
            for(Minigun m : miniguns) {
                updateMinigun(m);
            }
            for(Pod p : pods) {
                updatePod(p);
            }
            
            for(TextSet t : signs) {
                if(t.distanceTo(robot.activeBounds.getLocation()) < currentTextDist || currentTextSet == emptyText) {
                    if(t != currentTextSet) {
                        currentTextSet.finish();
                        currentTextSet = t;
                        currentTextDist = t.distanceTo(robot.activeBounds.getLocation());
                    }
                }
            }
            currentTextSet.update();
            
            if(robot.health < 1) {
                robot.activeBounds.setX(checkpoint.x);
                robot.activeBounds.setY(checkpoint.y);
                robot.xVelocity = 0;
                robot.yVelocity = 0;
                robot.health = 3;
                if(robot.activeBounds == robot.large) { robot.transform(tiles, false); }
                roboDestroy.play();
            }
        }
        
    }
    
    @Override
    public void iloop() {
        if(Keys.pressed(KeyEvent.VK_S)) { 
            //if(animswitch) { test.transition(6, 0); } else { test.transition(0, 6); } 
            //animswitch = !animswitch;
            robot.transform(tiles, true);
        }
        if(Keys.pressed(KeyEvent.VK_W) && robot.yAcceleration == 0 && robot.jump == 0) {
            robot.jump = robot.jumpHeight;
            roboJump.play();
        }
        if(Keys.pressed(KeyEvent.VK_A)) {
            robot.xAcceleration = -0.3f;
        }
        else if(Keys.pressed(KeyEvent.VK_D)) {
            robot.xAcceleration = 0.3f;
        } else {
            if(robot.xAcceleration != 0) {
                if(Math.abs(robot.xVelocity) < 0.4) {
                    robot.xVelocity = 0;
                    robot.xAcceleration = 0;
                }
                else {
                    robot.xAcceleration = (robot.xVelocity < 0) ? 0.3f : -0.3f;
                }
            }
        }
        if(Keys.pressed(KeyEvent.VK_SPACE) && robot.activeBounds == robot.large) {
            robot.firing = true;
            robot.createBullets(bullets);
        } else {
            robot.firing = false;
        }
        if(Keys.pressed(KeyEvent.VK_P)) {
            music.stop();
            Transition.transition(this, LevelSelect.getLevelSelect(parent));
        }
        if(Keys.pressed(KeyEvent.VK_DOWN) && currentTextSet.complete) {
            currentTextSet.begin();
        }
    }
    
}