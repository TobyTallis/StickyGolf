package com.tobytallis.stickygolf;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StickyGolf implements ApplicationListener, InputProcessor, Screen {

    private final float PIXELS_TO_METRES;
    private boolean ballTouched;
    private OrthographicCamera camera;
    private Color[] colours;
    private Matrix4 debugMatrix;
    private Matrix4 screenMatrix;
    private Box2DDebugRenderer debugRenderer;
    private boolean drawTrajectory;
    private float forceX;
    private float forceY;
    private final Screens game;
    private GolfBall golfBall;
    private Body golfBallBody;
    private Level currLevel;
    private MovingPlatform mPlatform001 = new MovingPlatform(1300, 400, 2000, 800, 200, 60, 5);
    private Level level00 = new Level(0, 4000, 8000, 300, 400, new Platform[]{new Platform(500, 1200, 300, 60, new int[]{180, 9, 230}), new Platform(1600, 1800, 600, 60, new int[]{430, 8, 200}), new Platform(1100, 200, 60, 600), new Platform(3300, 600, 300, 60, new int[]{50, 3, 170, 200, 1, 200}), new Platform(3300, 1000, 700, 60, new int[]{550, 5, 180}), new Platform(2800, 1200, 100, 59), new Platform(2800, 1259, 100, 1, 0, true, new int[0], 0, true), new Platform(2800, 200, 900, 60, new int[]{300, 12, 220, 700, 2, 180}), new Platform(100, 4000, 3900, 60, new int[]{100, 0, 100, 200, 1, 100, 300, 2, 100, 400, 3, 100, 500, 4, 100, 600, 5, 100, 700, 6, 100, 800, 7, 100, 900, 8, 100, 1000, 9, 100, 1100, 10, 100, 1200, 11, 100, 1300, 12, 100, 1400, 13, 100, 1500, 14, 100, 1600, 15, 100, 1700, 16, 100, 1800, 17, 100, 1900, 18, 100, 2000, 19, 100, 2100, 20, 100, 2200, 21, 100, 2300, 22, 100, 2400, 23, 100, 2500, 24, 100, 2600, 25, 100, 2700, 26, 100}), new Platform(0, 0, 4000, 60, 0, false, new int[]{200, 2, 220, 800, 4, 140, 2400, 11, 230}, 0, false), new Platform(0, 0, 60, 8000, false), new Platform(3940, 0, 60, 8000, false), new Platform(0, 7940, 4000, 60, false), new Platform(300, 6000, 600, 60, 20, true, new int[0], 0, false)}, new Box[]{new Box(500, 300, 50, 50, 0), new Box(600, 300, 50, 50, 0), new Box(500, 380, 50, 50, 0), new Box(2400, 6060, 50, 50, 0), new Box(600, 6100, 50, 50, 0), new Box(700, 300, 50, 50, 0)}, new Switch[]{new Switch(1650, 1900, 60, 1), new Switch(800, 400, 60, 1), new Switch(300, 1600, 60, 2), new Switch(2300, 6300, 60, 3)}, new Door[]{new Door(1115, 60, 1115, 200, 30, 140, 1), new Door(500, 1800, 500, 2200, 30, 400, 2)}, new MovingPlatform[]{new MovingPlatform(600, 5000, 2800, 5000, 200, 60, 5, 2), mPlatform001, new MovingPlatform(400, 7000, 1600, 6200, 200, 60, 3, 30, 2, true, new int[0], true, 0)}, new Text[]{new Text(2, "World 0, Level 0", 200, 400), new Text(1, "this is a", 670, 500), new Text(2, "switch", 670, 470), new Text(1, "it opens", 920, 200), new Text(1, "this", 920, 170), new Text(2, "door", 967, 170), new Text(1, "this", 0, 200, mPlatform001), new Text(2, "platform", 45, 201, mPlatform001), new Text(1, "moves", 30, 170, mPlatform001)});
    private Box box111 = new Box(625, 630, 50, 50, 0);
    private Level level11 = new Level(1, 1000, 1000, 100, 200, new Platform[]{new Platform(-60, -60, 1120, 60, false), new Platform(-60, 0, 60, 1000, false), new Platform(1000, 0, 60, 1000, false), new Platform(-60, 1000, 1120, 60, false), new Platform(-60, 100, 360, 60), new Platform(300, 500, 100, 60), new Platform(450, 550, 100, 60), new Platform(600, 600, 100, 60), new Platform(800, 700, 200, 59), new Platform(800, 759, 200, 1, 0, true, new int[0], 0, true), new Platform(200, 500, 60, 300)}, new Box[]{box111, new Box(710, 300, 150, 80, 0)}, new Switch[]{new Switch(300, 800, 60, 1, false)}, new Door[]{new Door(770, 700, 770, 1000, 30, 300, 1)}, new MovingPlatform[0], new Text[]{new Text(2, "World 1, Level 1", 20, 40), new Text(1, "this", -30, 50, box111), new Text(2, "box", -10, 47, null, box111), new Text(1, "looks fun", -30, 20, box111)});
    private Level level12 = new Level(1, 3000, 3000, 100, 200, new Platform[]{new Platform(-60, -60, 3120, 60, false), new Platform(-60, 0, 60, 3000, false), new Platform(3000, 0, 60, 3000, false), new Platform(-60, 3000, 3120, 60, false), new Platform(-60, 970, 2060, 60), new Platform(1000, 1970, 1700, 60), new Platform(2900, 1970, 100, 60), new Platform(2700, 1970, 200, 59), new Platform(2700, 2029, 200, 1, 0, true, new int[0], 0, true)}, new Box[]{new Box(200, 300, 50, 50, 0), new Box(1470, 0, 60, 950, 0), new Box(1470, 1030, 60, 900, 0)}, new Switch[0], new Door[0], new MovingPlatform[]{new MovingPlatform(2700, 300, 2700, 1700, 180, 60, 6), new MovingPlatform(120, 1330, 120, 2730, 180, 60, 6)}, new Text[]{new Text(2, "World 1, Level 2", 300, 400)});
    private Level[] levels = new Level[]{level00, level11, level12};
    //private ArrayList<Texture> platformTextures = new ArrayList<Texture>();
    private ArrayList<Body> platformBodies = new ArrayList<Body>();
    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private ArrayList<Body> movingPlatformBodies = new ArrayList<Body>();
    private ArrayList<MovingPlatform> movingPlatforms = new ArrayList<MovingPlatform>();
    private ArrayList<Body> doorBodies = new ArrayList<Body>();
    private ArrayList<Door> doors = new ArrayList<Door>();
    private ArrayList<Texture> boxTextures = new ArrayList<Texture>();
    private ArrayList<Body> boxBodies = new ArrayList<Body>();
    private ArrayList<Box> boxes = new ArrayList<Box>();
    private ArrayList<Tree> trees = new ArrayList<Tree>();
    private ArrayList<Switch> switches = new ArrayList<Switch>();
    private ArrayList<Text> texts = new ArrayList<Text>();
    private ArrayList<Button> UIButtons = new ArrayList<Button>();
    private HashMap<Switch, ArrayList<Door>> links = new HashMap<Switch, ArrayList<Door>>();
    private int screenH;
    private int screenW;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch worldSpriteBatch;
    private SpriteBatch screenSpriteBatch;
    private boolean stopMovement;
    private World world;
    private int worldH;
    private int worldW;
    private BitmapFont defaultFont;
    private BitmapFont thinFont;
    private BitmapFont boldFont;
    private Joint ropeJoint;
    private RopeJointDef ropeJointDef = new RopeJointDef();
    private ArrayList<PrismaticJoint> doorJoints = new ArrayList<PrismaticJoint>();
    private PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
    private Filter platformFilter;
    private Filter noCollisionFilter;
    private Filter doorFilter;
    private float step = 1/60f;
    private boolean slowmotion = false;
    private int applyForceCount = 0;
    private Vector2 desiredCameraCoords;
    private Texture restartButtonTexture;
    private Texture lookButtonTexture;
    private Texture lookButtonOffTexture;
    private Texture zoomInButtonTexture;
    private Texture zoomOutButtonTexture;
    private Texture powerUpButtonTexture;
    private Texture powerUpButtonOffTexture;
    private Texture golfBallTexture;
    private int pressedButton = -1;
    private boolean changeLevel = false;
    private Vector2 touchDownPosition;
    private Vector2 touchDownCameraPosition;
    private float lastCameraZoom = 1;

    public StickyGolf(Screens gam) {
        game = gam;
        ballTouched = false;
        drawTrajectory = false;
        stopMovement = false;
        shapeRenderer = new ShapeRenderer();
        golfBall = new GolfBall();
        colours = new Color[]{new Color(150 / 255f, 40 / 255f, 27 / 255f, 1), new Color(207 / 255f, 0, 15 / 255f, 1),
                new Color(210 / 255f, 77 / 255f, 87 / 255f, 1), new Color(242 / 255f, 38 / 255f, 19 / 255f, 1),
                new Color(217 / 255f, 30 / 255f, 24 / 255f, 1), new Color(239 / 255f, 72 / 255f, 54 / 255f, 1),
                new Color(214 / 255f, 69 / 255f, 65 / 255f, 1), new Color(192 / 255f, 57 / 255f, 43 / 255f, 1),
                new Color(231 / 255f, 76 / 255f, 60 / 255f, 1), new Color(211 / 255f, 84 / 255f, 0 / 255f, 1),
                new Color(249 / 255f, 105 / 255f, 14 / 255f, 1), new Color(242 / 255f, 121 / 255f, 53 / 255f, 1),
                new Color(232 / 255f, 126 / 255f, 4 / 255f, 1), new Color(242 / 255f, 120 / 255f, 75 / 255f, 1),
                new Color(248 / 255f, 148 / 255f, 6 / 255f, 1), new Color(230 / 255f, 126 / 255f, 34 / 255f, 1),
                new Color(235 / 255f, 151 / 255f, 78 / 255f, 1), new Color(235 / 255f, 149 / 255f, 50 / 255f, 1),
                new Color(243 / 255f, 156 / 255f, 18 / 255f, 1), new Color(244 / 255f, 179 / 255f, 80 / 255f, 1),
                new Color(245 / 255f, 171 / 255f, 53 / 255f, 1), new Color(249 / 255f, 191 / 255f, 59 / 255f, 1),
                new Color(233 / 255f, 212 / 255f, 96 / 255f, 1), new Color(253 / 255f, 227 / 255f, 167 / 255f, 1),
                new Color(247 / 255f, 202 / 255f, 24 / 255f, 1), new Color(244 / 255f, 208 / 255f, 63 / 255f, 1),
                new Color(245 / 255f, 215 / 255f, 110 / 255f, 1)};
        PIXELS_TO_METRES = 100;
        currLevel = levels[game.currLevel];
        Gdx.input.setInputProcessor(this);
        worldSpriteBatch = new SpriteBatch();
        screenSpriteBatch = new SpriteBatch();
        System.out.println("LOADING FONTS");
        defaultFont = game.assetManager.get("Roboto-Medium.ttf", BitmapFont.class);
        thinFont = game.assetManager.get("Roboto-Thin.ttf", BitmapFont.class);
        boldFont = game.assetManager.get("Roboto-Black.ttf", BitmapFont.class);
        System.out.println("LOADING TEXTURES");
        golfBallTexture = game.assetManager.get("GolfBall512.png", Texture.class);
        lookButtonTexture = game.assetManager.get("LookButton512.png", Texture.class);
        lookButtonOffTexture = game.assetManager.get("LookButtonOff512.png", Texture.class);
        zoomInButtonTexture = game.assetManager.get("ZoomInButton512.png", Texture.class);
        zoomOutButtonTexture = game.assetManager.get("ZoomOutButton512.png", Texture.class);
        powerUpButtonTexture = game.assetManager.get("PowerUpButton512.png", Texture.class);
        powerUpButtonOffTexture = game.assetManager.get("PowerUpButtonOff512.png", Texture.class);
        restartButtonTexture = game.assetManager.get("RestartButton512.png", Texture.class);
        //restartButtonTexture = new Texture(Gdx.files.internal("RestartButton512.png"), true);
        //restartButtonTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        System.out.println("LOADED");
    }

    public void create() {
    }

    public void show() {
        System.out.println("SHOWN");
    }

    public void render(float delta) {
        if (applyForceCount > 0) {
            golfBallBody.applyForceToCenter(forceX, forceY, true);
            applyForceCount -= 1;
        }
        if (golfBall.cameraFollow) {
            camera.viewportHeight = screenH;
            camera.viewportWidth = screenW;
            camera.position.x += ((golfBall.x) - camera.position.x) / 10;
            camera.position.y += ((golfBall.y) - camera.position.y) / 10;
        } else {
            desiredCameraCoords.x = Math.min(Math.max(desiredCameraCoords.x, 0), worldW);
            desiredCameraCoords.y = Math.min(Math.max(desiredCameraCoords.y, 0), worldH);
            camera.position.x += (desiredCameraCoords.x - camera.position.x) / 10;
            camera.position.y += (desiredCameraCoords.y - camera.position.y) / 10;
        }
        camera.update();
        world.step(step, 6, 2);
        debugMatrix = new Matrix4(camera.combined);
        debugMatrix.scale(PIXELS_TO_METRES, PIXELS_TO_METRES, 1);
        if (stopMovement) {
            //golfBallBody.setLinearVelocity(0, 0);
            //golfBallBody.setAngularVelocity(0);
            ///golfBallBody.setActive(false);
            ropeJoint = world.createJoint(ropeJointDef);
            golfBall.airHits = 0;
            stopMovement = false;
        }
        golfBall.x = (golfBallBody.getPosition().x * PIXELS_TO_METRES);
        golfBall.y = (golfBallBody.getPosition().y * PIXELS_TO_METRES);
        Gdx.gl.glClear(16640);
        Gdx.gl.glClearColor(150 / 255f, 40 / 255f, 27 / 255f, 1);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(150 / 255f, 40 / 255f, 27 / 255f, 1);
        shapeRenderer.rect(-worldW, -worldH, 3*worldW, 3*worldH);
        if (slowmotion) {
            Color color = new Color(colours[(int) ((System.currentTimeMillis() / 1000) % colours.length)]);
            Color color1 = new Color(colours[(int) ((System.currentTimeMillis() / 1000 + 1) % colours.length)]);
            shapeRenderer.setColor(color.lerp(color1, (System.currentTimeMillis() % 1000) / 1000f));
        } else {
            shapeRenderer.setColor(238 / 255f, 238 / 255f, 238 / 255f, 1);
        }
        shapeRenderer.rect(0, 0, worldW, worldH);
        // draw shadows
        shapeRenderer.setColor(shapeRenderer.getColor().add(-38 / 255f, -38 / 255f, -38 / 255f, 0));
        for (int i = 0; i < doors.size(); i++) {
            Door d = doors.get(i);
            Body b = doorBodies.get(i);
            if (d.justChanged) {
                if (ropeJointDef.bodyA == b || ropeJointDef.bodyB == b) {
                    if (golfBallBody.getJointList().size > 0) {
                        world.destroyJoint(ropeJoint);
                    }
                }
                d.justChanged = false;
            }
            d.x = ((PIXELS_TO_METRES * b.getPosition().x) - (d.width / 2));
            d.y = ((PIXELS_TO_METRES * b.getPosition().y) - (d.height / 2));
            float x = d.x;
            float y = d.y;
            if (d.shadow) {
                shapeRenderer.rect(x + game.shadowX, y - game.shadowY, d.width / 2, d.height / 2, d.width, d.height, 1, 1, d.angle);
            }
        }
        for (Platform p : platforms) {
            float x = p.x;
            float y = p.y;
            if (p.shadow) {
                shapeRenderer.rect(x + game.shadowX, y - game.shadowY, p.width / 2, p.height / 2, p.width, p.height, 1, 1, p.angle);
            }
            if (p.finalPlatform) {
                shapeRenderer.rect(x + p.width/2 - 5 + game.shadowX, y + p.height - game.shadowY, 10, 60);
                shapeRenderer.triangle(x + p.width / 2 + 5 + game.shadowX, y + p.height + 60 - game.shadowY, x + p.width / 2 + 5 + game.shadowX,
                        y + p.height + 80 - game.shadowY, x + p.width / 2 - 45 + game.shadowX, y + p.height + 60 - game.shadowY);
            }
        }
        for (int i = 0; i < movingPlatforms.size(); i++) {
            MovingPlatform m = movingPlatforms.get(i);
            Body d = movingPlatformBodies.get(i);
            m.x = ((PIXELS_TO_METRES * d.getPosition().x) - (m.width / 2));
            m.y = ((PIXELS_TO_METRES * d.getPosition().y) - (m.height / 2));
            float x = m.x;
            float y = m.y;
            if ((m.startX == m.endX && (y >= m.endY || y <= m.startY)) || (m.startX != m.endX && (x >= m.endX || x <= m.startX))) {
                d.setLinearVelocity(d.getLinearVelocity().scl(-1));
            }
            if (m.shadow) {
                if (m.shape == 1) {
                    shapeRenderer.rect(x + game.shadowX, y - game.shadowY, m.width / 2, m.height / 2, m.width, m.height, 1, 1, m.angle);
                } else if (m.shape == 2) {
                    shapeRenderer.rect(x + m.width - m.height / 3 + game.shadowX, y + m.height * 2 / 3 - game.shadowY, m.height / 3 - m.width / 2, -m.height / 6, m.height / 3, m.height / 3, 1, 1, m.angle);
                    shapeRenderer.rect(x + game.shadowX, y + m.height * 2 / 3 - game.shadowY, m.width / 2, -m.height / 6, m.height / 3, m.height / 3, 1, 1, m.angle);
                    shapeRenderer.rect(x + game.shadowX, y - game.shadowY, m.width / 2, m.height / 2, m.width, m.height * 2 / 3, 1, 1, m.angle);
                }
            }
        }
        for (Tree t : trees) {
            float x = t.x;
            float y = t.y;
            shapeRenderer.rect(x - t.size / 24 + game.shadowX, y - game.shadowY, t.size / 12, t.size / 10);
            shapeRenderer.triangle(x - t.size / 2 + game.shadowX, y + t.size / 10 - game.shadowY, x + t.size / 2 + game.shadowX,
                    y + t.size / 10 - game.shadowY, x + game.shadowX, y + t.size - game.shadowY);
        }
        for (int i = 0; i < boxes.size(); i++) {
            Box b = boxes.get(i);
            Body d = boxBodies.get(i);
            b.x = ((PIXELS_TO_METRES * d.getPosition().x) - (b.width / 2));
            b.y = ((PIXELS_TO_METRES * d.getPosition().y) - (b.height / 2));
            float x = b.x;
            float y = b.y;
            b.angle = (float) (((d.getAngle() * 180)) / Math.PI) % 360;
            shapeRenderer.rect(x + game.shadowX, y - game.shadowY, (b.width / 2), (b.height / 2), b.width, b.height, 1, 1, b.angle);
        }
        shapeRenderer.circle(golfBall.x + game.shadowX, golfBall.y - game.shadowY, ((golfBall.radius * 4) / 3));
        shapeRenderer.end();
        // draw background font
        worldSpriteBatch.begin();
        worldSpriteBatch.setProjectionMatrix(camera.combined);
        for (Text t : texts) {
            float x = t.x;
            float y = t.y;
            if (t.platformToFollow != null) {
                x += t.platformToFollow.x;
                y += t.platformToFollow.y;
            } else if (t.boxToFollow != null) {
                x += t.boxToFollow.x;
                y += t.boxToFollow.y;
            }
            if (t.font == 1) {
                thinFont.draw(worldSpriteBatch, t.text, x, y);
            } else if (t.font == 2) {
                defaultFont.draw(worldSpriteBatch, t.text, x, y);
            } else if (t.font == 3) {
                boldFont.draw(worldSpriteBatch, t.text, x, y);
            }
        }
        worldSpriteBatch.end();
        // draw background objects
        shapeRenderer.begin(ShapeType.Filled);
        for (Switch s : switches) {
            float x = s.x - s.size/2;
            float y = s.y - s.size/2;
            shapeRenderer.setColor(108 / 255f, 122 / 255f, 137 / 255f, 1);
            shapeRenderer.rect(x, y, s.size, s.size);
            if (s.isPressed) {
                shapeRenderer.setColor(0 / 255f, 238 / 255f, 23 / 255f, 1);
            } else {
                shapeRenderer.setColor(238 / 255f, 0 / 255f, 23 / 255f, 1);
            }
            shapeRenderer.rect(x + (s.size / 8), y + (s.size / 8), s.size * 3 / 4, s.size * 3 / 4);
        }
        // draw foreground objects
        for (Door d : doors) {
            float x = d.x;
            float y = d.y;
            shapeRenderer.setColor(22 / 255f, 160 / 255f, 133 / 255f, 1);
            shapeRenderer.rect(x, y, d.width / 2, d.height / 2, d.width, d.height, 1, 1, d.angle);
        }
        for (Platform p : platforms) {
            float x = p.x;
            float y = p.y;
            if (p.finalPlatform) {
                shapeRenderer.setColor(1, 1, 1, 1);
                shapeRenderer.rect(x + p.width/2 - 5, y + p.height, 10, 60);
                shapeRenderer.setColor(colours[1]);
                shapeRenderer.triangle(x + p.width / 2 + 5, y + p.height + 60, x + p.width / 2 + 5, y + p.height + 80, x + p.width / 2 - 45, y + p.height + 60);
            }
            shapeRenderer.setColor(150 / 255f, 40 / 255f, 27 / 255f, 1);
            shapeRenderer.rect(x, y, p.width / 2, p.height / 2, p.width, p.height, 1, 1, p.angle);
        }
        for (MovingPlatform m : movingPlatforms) {
            float x = m.x;
            float y = m.y;
            shapeRenderer.setColor(150 / 255f, 40 / 255f, 27 / 255f, 1);
            if (m.shape == 1) {
                shapeRenderer.rect(x, y, m.width / 2, m.height / 2, m.width, m.height, 1, 1, m.angle);
            } else if (m.shape == 2) {
                shapeRenderer.rect(x + m.width - m.height / 3, y + m.height * 2 / 3, m.height / 3 - m.width / 2, -m.height / 6, m.height / 3, m.height / 3, 1, 1, m.angle);
                shapeRenderer.rect(x, y + m.height * 2 / 3, m.width / 2, -m.height / 6, m.height / 3, m.height / 3, 1, 1, m.angle);
                shapeRenderer.rect(x, y, m.width / 2, m.height / 2, m.width, m.height * 2 / 3, 1, 1, m.angle);
            }
        }
        for (Tree t : trees) {
            float x = t.x;
            float y = t.y;
            shapeRenderer.setColor(colours[t.colour]);
            shapeRenderer.rect(x - t.size / 24, y, t.size / 12, t.size / 10);
            shapeRenderer.triangle(x - t.size / 2, y + t.size / 10, x + t.size / 2, y + t.size / 10, x, y + t.size);
        }/*
        for (Box b : boxes) {
            float x = b.x;
            float y = b.y;
            shapeRenderer.setColor(31 / 255f, 58 / 255f, 147 / 255f, 1);
            shapeRenderer.rect(x, y, b.width / 2, b.height / 2, b.width, b.height, 1, 1, b.angle);
        }*/
        shapeRenderer.setColor(150 / 255f, 40 / 255f, 27 / 255f, 1);
        shapeRenderer.rect(0, -100, worldW, 100);
        shapeRenderer.rect(worldW, 0, 100, worldH);
        shapeRenderer.end();
        // draw golf ball and trajectory
        worldSpriteBatch.begin();
        worldSpriteBatch.setProjectionMatrix(camera.combined);
        /*for (int i = 0; i < platforms.size(); i++) {
            Platform p = platforms.get(i);
            Texture t = platformTextures.get(i);
            worldSpriteBatch.draw(t, p.x, p.y, p.width/2, p.height/2, p.width, p.height, 1, 1, p.angle, 0, 0, t.getWidth(), t.getHeight(), false, false);
        }*/
        for (int i = 0; i < boxes.size(); i++) {
            Box b = boxes.get(i);
            Texture t = boxTextures.get(i);
            worldSpriteBatch.draw(t, b.x, b.y, b.width/2, b.height/2, b.width, b.height, 1, 1, b.angle, 0, 0, t.getWidth(), t.getHeight(), false, false);
        }
        if (drawTrajectory) {
            Vector2 golfBallVelocity;
            if (slowmotion) {
                golfBallVelocity = new Vector2(golfBallBody.getLinearVelocity().x + forceX / 3, golfBallBody.getLinearVelocity().y + forceY / 3);
            } else {
                golfBallVelocity = new Vector2(forceX / 3, forceY / 3);
            }
            for (int i = 1; i < golfBall.view; i++) {
                Vector2 trajectoryPoint = getTrajectoryPoint(golfBallBody.getPosition(), golfBallVelocity, i);
                worldSpriteBatch.draw(golfBallTexture, (trajectoryPoint.x * PIXELS_TO_METRES) - (golfBall.radius / 3), (trajectoryPoint.y * PIXELS_TO_METRES) - (golfBall.radius / 3), golfBall.radius / 3, golfBall.radius / 3, golfBall.radius * 2 / 3, golfBall.radius * 2 / 3, 1, 1, golfBallBody.getAngle(), 0, 0, golfBallTexture.getWidth(), golfBallTexture.getHeight(), false, false);
            }
        }
        golfBall.angle = (float) (((golfBallBody.getAngle() * 180)) / Math.PI) % 360;
        worldSpriteBatch.draw(golfBallTexture, golfBall.x - golfBall.radius, golfBall.y - golfBall.radius, golfBall.radius, golfBall.radius, golfBall.radius * 2, golfBall.radius * 2, 1, 1, golfBall.angle, 0, 0, golfBallTexture.getWidth(), golfBallTexture.getHeight(), false, false);
        worldSpriteBatch.end();
        // draw UI elements
        UIButtons.get(1).texture = golfBall.cameraFollow ? lookButtonTexture : lookButtonOffTexture;
        UIButtons.get(2).texture = !slowmotion ? powerUpButtonTexture : powerUpButtonOffTexture;
        screenSpriteBatch.begin();
        for (Button b : UIButtons) {
            if (b.enabled) {
                float x = b.x * Gdx.graphics.getWidth() / screenW;
                float y = b.y * Gdx.graphics.getHeight() / screenH;
                float radius = b.radius * Gdx.graphics.getWidth() / screenW;
                screenSpriteBatch.draw(b.texture, x - radius, y - radius, b.radius, radius, radius * 2, radius * 2, 1, 1, b.angle, 0, 0, b.texture.getWidth(), b.texture.getHeight(), false, false);
            }
        }
        screenSpriteBatch.end();
        //lights.get(0).setPosition(golfBallBody.getPosition());
        debugRenderer.render(world, debugMatrix);
        //rayHandler.setCombinedMatrix(debugMatrix);
        //rayHandler.updateAndRender();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        //screenSpriteBatch.begin();
        //text.draw(screenSpriteBatch, "X: " + ((int) golfBall.x), (Gdx.graphics.getWidth() - 190), (Gdx.graphics.getHeight() - 30));
        //text.draw(screenSpriteBatch, "Y: " + ((int) golfBall.y), (Gdx.graphics.getWidth() - 190), (Gdx.graphics.getHeight() - 70));
        //text.draw(screenSpriteBatch, "Camera X: " + ((int) camera.position.x), (Gdx.graphics.getWidth() - 300), (Gdx.graphics.getHeight() - 110));
        //text.draw(screenSpriteBatch, "Camera Y: " + ((int) camera.position.y), (Gdx.graphics.getWidth() - 300), (Gdx.graphics.getHeight() - 150));
        //screenSpriteBatch.end();
        if (changeLevel) {
            game.currLevel = game.currLevel == levels.length -1 ? 0 : game.currLevel + 1;
            game.setScreen(new StickyGolf(game));
        }
    }

    public void resize(int width, int height) {
        System.out.println("RESIZING");
        screenH = 1280;
        screenW = 720;
        game.clearWorld();
        game.clearFonts();
        System.out.println("CURRENT LEVEL: " + game.currLevel);
        worldH = currLevel.height;
        worldW = currLevel.width;
        golfBall.x = currLevel.startX;
        golfBall.y = currLevel.startY;
        golfBall.radius = 18;
        platforms.clear();
        platforms.addAll(Arrays.asList(currLevel.platforms));
        doors.clear();
        doors.addAll(Arrays.asList(currLevel.doors));
        doorJoints.clear();
        movingPlatforms.clear();
        movingPlatforms.addAll(Arrays.asList(currLevel.movingPlatforms));
        boxes.clear();
        boxes.addAll(Arrays.asList(currLevel.boxes));
        switches.clear();
        switches.addAll(Arrays.asList(currLevel.switches));
        texts.clear();
        texts.addAll(Arrays.asList(currLevel.texts));
        UIButtons.clear();
        UIButtons.add(new Button(screenW - Gdx.graphics.getWidth()/12, screenH - Gdx.graphics.getWidth()/12, Gdx.graphics.getWidth()/12, 0, restartButtonTexture, 0));
        UIButtons.add(new Button(screenW - Gdx.graphics.getWidth()/12, Gdx.graphics.getWidth()/12, Gdx.graphics.getWidth()/12, 0, lookButtonTexture, 1));
        UIButtons.add(new Button(screenW - Gdx.graphics.getWidth()/4, Gdx.graphics.getWidth()/12, Gdx.graphics.getWidth()/12, 0, powerUpButtonTexture, 2));
        UIButtons.add(new Button(screenW - Gdx.graphics.getWidth()/12, Gdx.graphics.getWidth()*5/12, Gdx.graphics.getWidth()/12, 0, zoomInButtonTexture, 3, false));
        UIButtons.add(new Button(screenW - Gdx.graphics.getWidth()/12, Gdx.graphics.getWidth()/4, Gdx.graphics.getWidth()/12, 0, zoomOutButtonTexture, 4, false));
        trees.clear();
        for (Platform p : platforms) {
            for (int i = 0; i < p.trees.length; i += 3) {
                trees.add(new Tree(p.x + p.trees[i], p.y + p.height, p.trees[i+1], p.trees[i+2]));
            }
        }
        for (Door d : doors) {
            for (Switch s : switches) {
                if (d.link == s.link) {
                    ArrayList<Door> dList = new ArrayList<Door>();
                    try {
                        dList.addAll(links.get(s));
                    } catch (NullPointerException e) {}
                    dList.add(d);
                    links.put(s, dList);
                }
            }
        }
        System.out.println("BOX2D START");
        initBox2DGame();
        System.out.println("BOX2D END");
        System.out.println("BODY COUNT: " + world.getBodyCount());
        camera.viewportWidth = screenW;
        camera.viewportHeight = screenH;
    }

    public void render() {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void hide() {
    }

    public void dispose() {
        System.out.println("DISPOSING");
        //world.dispose();
        shapeRenderer.dispose();
        screenSpriteBatch.dispose();
        worldSpriteBatch.dispose();
        //boldFont.dispose();
        //thinFont.dispose();
        //defaultFont.dispose();
        debugRenderer.dispose();
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean ballPressed = true;
        Vector2 worldCoords = getWorldCoords(new Vector2(screenX, Gdx.graphics.getHeight() - screenY));
        for (Switch s : switches) {
            if (worldCoords.x >= s.x - s.size/2 && worldCoords.x <= s.x + s.size/2 && worldCoords.y >= s.y - s.size/2 && worldCoords.y <= s.y + s.size/2 && golfBall.cameraFollow) {
                ballPressed = false;
            }
        }
        if (pressedButton == -1 && ballPressed) {
            for (Button b : UIButtons) {
                float x = b.x * Gdx.graphics.getWidth() / screenW;
                float y = b.y * Gdx.graphics.getHeight() / screenH;
                float radius = b.radius * Gdx.graphics.getWidth()/ screenW;
                if ((screenX < x + radius && screenX > x - radius) && (Gdx.graphics.getHeight() - screenY < y + radius && Gdx.graphics.getHeight() - screenY > y - radius)) {
                    ballPressed = false;
                }
            }
        }
        if (ballPressed) {
            if (golfBall.cameraFollow) {
                ballTouched = true;
            } else {
                touchDownPosition = worldCoords;
                touchDownCameraPosition = new Vector2(camera.position.x, camera.position.y);
            }
        }
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 worldCoords = getWorldCoords(new Vector2(screenX, Gdx.graphics.getHeight() - screenY));
        Vector2 golfBallCoords = new Vector2(golfBall.x, golfBall.y);
        for (Switch s : switches) {
            if (worldCoords.x >= s.x - s.size/2 && worldCoords.x <= s.x + s.size/2 && worldCoords.y >= s.y - s.size/2 && worldCoords.y <= s.y + s.size/2 && golfBall.cameraFollow) {
                s.isPressed = !s.isPressed;
                for (Door d : links.get(s)) {
                    d.isOpen = !d.isOpen;
                    d.justChanged = true;
                    d.joint.setMotorSpeed(-d.joint.getMotorSpeed());
                }
            }
        }
        if (pressedButton == -1) {
            for (Button b : UIButtons) {
                float x = b.x * Gdx.graphics.getWidth() / screenW;
                float y = b.y * Gdx.graphics.getHeight() / screenH;
                float radius = b.radius * Gdx.graphics.getWidth()/ screenW;
                if ((b.enabled) && (screenX < x + radius && screenX > x - radius) && (Gdx.graphics.getHeight() - screenY < y + radius && Gdx.graphics.getHeight() - screenY > y - radius)) {
                    switch (b.target) {
                        case 0:
                            this.dispose();
                            game.setScreen(new StickyGolf(game));
                        case 1:
                            golfBall.cameraFollow = !golfBall.cameraFollow;
                            desiredCameraCoords = golfBallCoords;
                            UIButtons.get(3).enabled = !UIButtons.get(3).enabled;
                            UIButtons.get(4).enabled = !UIButtons.get(4).enabled;
                            lastCameraZoom = camera.zoom == 1 ? lastCameraZoom : camera.zoom;
                            camera.zoom = camera.zoom == 1 ? lastCameraZoom : 1;
                            break;
                        case 2:
                            slowmotion = !slowmotion;
                            if (slowmotion) {
                                step = 1 / 300f;
                            } else {
                                step = 1 / 60f;
                            }
                            break;
                        case 3:
                            camera.zoom -= camera.zoom > 0.5f ? 0.2f : 0;
                            break;
                        case 4:
                            camera.zoom += camera.zoom < 2f ? 0.2f : 0;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        if (ballTouched && drawTrajectory) {
            if (golfBallBody.getJointList().size == 0) {
                golfBall.airHits++;
            } else {
                golfBall.hits++;
                System.out.println(golfBall.hits);
                texts.add(new Text(1, Integer.toString(golfBall.hits), (int) golfBall.x, (int) golfBall.y));
            }
            if (golfBallBody.getJointList().size > 0) {
                world.destroyJoint(ropeJoint);
            }
            applyForceCount = (int) (1 / (step * 60));
        }
        ballTouched = false;
        drawTrajectory = false;
        return true;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 worldCoords = getWorldCoords(new Vector2(screenX, Gdx.graphics.getHeight() - screenY));
        if (ballTouched) {
            drawTrajectory = true;
            int powerMult = 10;
            if (golfBallBody.getJointList().size == 0) {
                if (golfBall.airHits < golfBall.maxAirHits || slowmotion) {
                    powerMult = 2;
                } else {
                    drawTrajectory = false;
                    powerMult = 0;
                }
            }
            forceX = ((Gdx.graphics.getWidth() / 2) - screenX) * (float) screenW / Gdx.graphics.getWidth() * powerMult / PIXELS_TO_METRES;
            forceY = (screenY - (Gdx.graphics.getHeight() / 2)) * (float) screenH / Gdx.graphics.getHeight() * powerMult / PIXELS_TO_METRES;
        } else if (!golfBall.cameraFollow) {
            //new Vector2((Gdx.graphics.getWidth() / screenW) * (Gdx.graphics.getWidth()/2 - screenX) + worldCoords.x,
             //   (Gdx.graphics.getHeight() / screenH) * (Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight() + screenY) + worldCoords.y);
            if (touchDownPosition != null) {
                desiredCameraCoords = new Vector2((touchDownPosition.x - worldCoords.x) * 5 * camera.zoom + touchDownCameraPosition.x, (touchDownPosition.y - worldCoords.y) * 3 * camera.zoom + touchDownCameraPosition.y);
            } else {
                touchDownPosition = worldCoords;
                touchDownCameraPosition = new Vector2(camera.position.x, camera.position.y);
            }
        }
        return true;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }

    private void initBox2DGame() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(screenW, screenH);
        camera.position.set(golfBall.x, golfBall.y, 0);
        world = game.world;
        //RayHandler.setGammaCorrection(true);
        //RayHandler.useDiffuseLight(true);

        //rayHandler = new RayHandler(world);
        //rayHandler.setAmbientLight(0.4f, 0.4f, 0.4f, 1);
        //rayHandler.setBlur(true);
        //rayHandler.setCulling(false);
        //rayHandler.setShadows(true);

        //lights.add(new PointLight(rayHandler, 200, Color.WHITE, 2000 / PIXELS_TO_METRES, 400 / PIXELS_TO_METRES, 300 / PIXELS_TO_METRES));
        //lights.add(new PointLight(rayHandler, 128, Color.WHITE, 2000 / PIXELS_TO_METRES, 1200 / PIXELS_TO_METRES, 700 / PIXELS_TO_METRES));
        //lights.add(new PointLight(rayHandler, 128, Color.WHITE, 1800 / PIXELS_TO_METRES, 800 / PIXELS_TO_METRES, 2700 / PIXELS_TO_METRES));
        //lights.add(new DirectionalLight(rayHandler, 200, Color.WHITE, 300));
        //lights.get(0).setContactFilter((short) 0x0008, (short) 0x0000, (short) -1);
        //lights.get(0).setSoft(false);

        screenMatrix = new Matrix4();
        screenMatrix.scl(screenW, screenH, 1);

        platformFilter = new Filter();
        platformFilter.categoryBits = 0x0002;
        platformFilter.maskBits = 0x0001 | 0x0002 | 0x0004;

        doorFilter = new Filter();
        doorFilter.categoryBits = 0x0008;
        doorFilter.maskBits = 0x0001 | 0x0004 | 0x0008;

        noCollisionFilter = new Filter();
        noCollisionFilter.categoryBits = 0x0000;
        noCollisionFilter.maskBits = 0x0000;

        BodyDef golfBallBodyDef = new BodyDef();
        golfBallBodyDef.type = BodyType.DynamicBody;
        golfBallBodyDef.position.set((golfBall.x) / PIXELS_TO_METRES, (golfBall.y) / PIXELS_TO_METRES);
        golfBallBody = world.createBody(golfBallBodyDef);
        Shape golfBallShape = new CircleShape();
        golfBallShape.setRadius(golfBall.radius / PIXELS_TO_METRES);
        FixtureDef golfBallFixtureDef = new FixtureDef();
        golfBallFixtureDef.shape = golfBallShape;
        golfBallFixtureDef.density = 0.5f;
        golfBallFixtureDef.restitution = 0;
        golfBallFixtureDef.friction = 1;
        golfBallFixtureDef.filter.categoryBits = 0x0001;
        golfBallFixtureDef.filter.maskBits = -1;
        golfBallBody.createFixture(golfBallFixtureDef);
        golfBallBody.setUserData(golfBall);
        golfBallShape.dispose();

        BodyDef platformBodyDef = new BodyDef();
        platformBodyDef.type = BodyType.StaticBody;
        platformBodyDef.bullet = true;
        PolygonShape platformShape = new PolygonShape();
        FixtureDef platformFixtureDef = new FixtureDef();
        platformFixtureDef.density = 1;
        platformFixtureDef.restitution = 0;
        platformFixtureDef.friction = 1;
        platformFixtureDef.filter.categoryBits = platformFilter.categoryBits;
        platformFixtureDef.filter.maskBits = platformFilter.maskBits;
        for (int i = 0; i < platforms.size(); i++) {
            Platform p = platforms.get(i);
            float w = p.width / PIXELS_TO_METRES;
            float h = p.height / PIXELS_TO_METRES;
            platformBodyDef.position.set(p.x / PIXELS_TO_METRES + w / 2, p.y / PIXELS_TO_METRES + h / 2);
            platformBodyDef.angle = (float) (p.angle * Math.PI) / 180;
            Body platformBody = world.createBody(platformBodyDef);
            platformShape.setAsBox(w / 2, h / 2, new Vector2(0, 0), 0);
            platformFixtureDef.shape = platformShape;
            platformBody.createFixture(platformFixtureDef);
            platformBody.setUserData(p);
            platformBody.getFixtureList().get(0).setFilterData(platformFilter);
            platformBodies.add(platformBody);
            /*Pixmap platformPixmap = new Pixmap(p.width, p.height, Pixmap.Format.RGBA8888);
            platformPixmap.setColor(colours[0]);
            platformPixmap.fillRectangle(0, 0, p.width, p.height);
            Texture platformTexture = new Texture(platformPixmap);
            platformTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            platformTextures.add(platformTexture);*/
        }

        BodyDef movingPlatformBodyDef = new BodyDef();
        movingPlatformBodyDef.type = BodyType.KinematicBody;
        movingPlatformBodyDef.bullet = true;
        for (int i = 0; i < movingPlatforms.size(); i++) {
            MovingPlatform m = movingPlatforms.get(i);
            float w = m.width / PIXELS_TO_METRES;
            float h = m.height / PIXELS_TO_METRES;
            movingPlatformBodyDef.position.set(m.startX / PIXELS_TO_METRES + w / 2, m.startY / PIXELS_TO_METRES + h / 2);
            movingPlatformBodyDef.angle = (float) (m.angle * Math.PI) / 180;
            Body movingPlatformBody = world.createBody(movingPlatformBodyDef);
            if (m.shape == 1) {
                platformShape.setAsBox(w / 2, h / 2, new Vector2(0, 0), 0);
            } else if (m.shape == 2) {
                platformShape.setAsBox(h / 6, h / 6, new Vector2(h / 6 - w / 2, h / 3), 0);
                platformFixtureDef.shape = platformShape;
                movingPlatformBody.createFixture(platformFixtureDef);
                platformShape.setAsBox(h / 6, h / 6, new Vector2(-h / 6 + w / 2, h / 3), 0);
                platformFixtureDef.shape = platformShape;
                movingPlatformBody.createFixture(platformFixtureDef);
                platformShape.setAsBox(w / 2, h / 3, new Vector2(0, -h / 6), 0);
            }
            platformFixtureDef.shape = platformShape;
            movingPlatformBody.createFixture(platformFixtureDef);
            movingPlatformBody.setUserData(m);
            movingPlatformBody.setLinearVelocity((m.endX - m.startX) / (m.time * PIXELS_TO_METRES), (m.endY - m.startY) / (m.time * PIXELS_TO_METRES));
            movingPlatformBodies.add(movingPlatformBody);
        }

        BodyDef doorBodyDef = new BodyDef();
        doorBodyDef.type = BodyType.DynamicBody;
        doorBodyDef.fixedRotation = true;
        doorBodyDef.bullet = true;
        FixtureDef doorFixtureDef = new FixtureDef();
        doorFixtureDef.density = 1;
        doorFixtureDef.restitution = 0;
        doorFixtureDef.friction = 1;
        doorFixtureDef.filter.categoryBits = doorFilter.categoryBits;
        doorFixtureDef.filter.maskBits = doorFilter.maskBits;
        BodyDef doorHoldBodyDef = new BodyDef();
        doorHoldBodyDef.type = BodyType.StaticBody;
        doorHoldBodyDef.bullet = true;
        FixtureDef doorHoldFixtureDef = new FixtureDef();
        doorHoldFixtureDef.filter.categoryBits = noCollisionFilter.categoryBits;
        doorHoldFixtureDef.filter.maskBits = noCollisionFilter.maskBits;
        for (int i = 0; i < doors.size(); i++) {
            Door d = doors.get(i);
            float w = d.width / PIXELS_TO_METRES;
            float h = d.height / PIXELS_TO_METRES;
            doorBodyDef.position.set(d.x / PIXELS_TO_METRES + w / 2, d.y / PIXELS_TO_METRES + h / 2);
            doorBodyDef.angle = (float) (d.angle * Math.PI) / 180;
            Body doorBody = world.createBody(doorBodyDef);
            platformShape.setAsBox(w / 2, h / 2, new Vector2(0, 0), 0);
            doorFixtureDef.shape = platformShape;
            doorBody.createFixture(platformFixtureDef);
            doorBody.setUserData(d);
            doorBody.getFixtureList().get(0).setFilterData(doorFilter);
            doorBodies.add(doorBody);
            doorHoldBodyDef.position.set(d.openX / PIXELS_TO_METRES + w / 2, d.openY / PIXELS_TO_METRES + h /2);
            Body doorHoldBody = world.createBody(doorHoldBodyDef);
            platformShape.setAsBox(1 / PIXELS_TO_METRES, 1 / PIXELS_TO_METRES);
            doorHoldFixtureDef.shape = platformShape;
            doorHoldBody.createFixture(doorHoldFixtureDef);
            prismaticJointDef.initialize(doorBody, doorHoldBody, doorHoldBody.getWorldCenter(), new Vector2(doorHoldBody.getPosition().sub(doorBody.getPosition())).nor());
            prismaticJointDef.collideConnected = false;
            prismaticJointDef.enableLimit = true;
            prismaticJointDef.lowerTranslation = (float) -getDistance(doorHoldBody.getPosition(), doorBody.getPosition());
            prismaticJointDef.enableMotor = true;
            prismaticJointDef.motorSpeed = 10;
            prismaticJointDef.maxMotorForce = 50;
            d.joint = ((PrismaticJoint) world.createJoint(prismaticJointDef));
        }

        platformShape.dispose();

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = BodyType.DynamicBody;
        boxBodyDef.bullet = true;
        PolygonShape boxShape = new PolygonShape();
        FixtureDef boxFixtureDef = new FixtureDef();
        boxFixtureDef.density = 0.3f;
        boxFixtureDef.restitution = 0.3f;
        //boxFixtureDef.friction = 1;
        boxFixtureDef.filter.categoryBits = 0x0004;
        boxFixtureDef.filter.maskBits = -1;

        for (Box b : boxes) {
            float w = b.width / PIXELS_TO_METRES;
            float h = b.height / PIXELS_TO_METRES;
            boxBodyDef.position.set(b.x / PIXELS_TO_METRES + w / 2, b.y / PIXELS_TO_METRES + h / 2);
            Body boxBody = world.createBody(boxBodyDef);
            boxShape.setAsBox(w / 2, h / 2);
            boxFixtureDef.shape = boxShape;
            boxBody.createFixture(boxFixtureDef);
            boxBody.setUserData(b);
            boxBodies.add(boxBody);
            Pixmap boxPixmap = new Pixmap(b.width, b.height, Pixmap.Format.RGBA8888);
            boxPixmap.setColor(colours[13]);
            boxPixmap.fillRectangle(0, 0, b.width, b.height);
            Texture boxTexture = new Texture(boxPixmap);
            boxTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            boxTextures.add(boxTexture);
        }

        boxShape.dispose();

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                ArrayList<Body> bodies = new ArrayList<Body>(platformBodies);
                bodies.addAll(movingPlatformBodies);
                bodies.addAll(boxBodies);
                bodies.addAll(doorBodies);
                for (Body b : bodies) {
                    if (((contact.getFixtureA().getBody() == golfBallBody && contact.getFixtureB().getBody() == b) ||
                            (contact.getFixtureB().getBody() == golfBallBody && contact.getFixtureA().getBody() == b)) && golfBallBody.getJointList().size == 0) {
                        stopMovement = true;
                        ropeJointDef.bodyA = golfBallBody;
                        ropeJointDef.bodyB = b;
                        ropeJointDef.localAnchorA.set(golfBallBody.getLocalCenter());
                        ropeJointDef.localAnchorB.set(b.getLocalPoint(golfBallBody.getWorldCenter()));
                        ropeJointDef.collideConnected = true;
                        if (platformBodies.contains(b)) {
                            if (((Platform) b.getUserData()).finalPlatform) {
                                System.out.println("FINISHED");
                                changeLevel = true;
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        //for (Light light : lights) {
        //    light.setXray(false);
        //    light.setSoftnessLength(0f);
        //}

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private Vector2 getTrajectoryPoint(Vector2 startingPosition, Vector2 startingVelocity, float n) {
        Vector2 stepVelocity = new Vector2(startingVelocity).scl(1/60f, 1/60f).scl(n);
        Vector2 stepGravity = world.getGravity().scl(1/3600f, 1/3600f).scl(0.5f * ((n * n) + n));
        return startingPosition.add(stepVelocity.x, stepVelocity.y).add(stepGravity.x, stepGravity.y);
    }

    private Vector2 getWorldCoords(Vector2 screenCoords) {
        return new Vector2(camera.position.x + (screenCoords.x - Gdx.graphics.getWidth() / 2) * (screenW / Gdx.graphics.getWidth()),
                camera.position.y + (screenCoords.y - Gdx.graphics.getHeight() / 2) * (screenH / Gdx.graphics.getHeight()));
    }

    private Vector2 getScreenCoords(Vector2 worldCoords) {
        return new Vector2((worldCoords.x - camera.position.x) * (Gdx.graphics.getWidth() / screenW) + Gdx.graphics.getWidth() / 2,
                (worldCoords.y - camera.position.y) * (Gdx.graphics.getHeight() / screenH) + Gdx.graphics.getHeight() / 2);
    }

    private double getDistance(Vector2 obj1, Vector2 obj2) {
        return Math.sqrt(Math.pow((obj1.x - obj2.x), 2) + Math.pow((obj1.y - obj2.y), 2));
    }
}