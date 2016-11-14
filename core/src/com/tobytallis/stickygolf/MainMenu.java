package com.tobytallis.stickygolf;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

import java.util.ArrayList;

public class MainMenu implements ApplicationListener, InputProcessor, Screen {

    private final Screens game;
    private int screenW, screenH;

    //ArrayList<PointLight> lights = new ArrayList<PointLight>();
    //RayHandler rayHandler;
    private World world;
    private final float PIXELS_TO_METRES;
    private Matrix4 debugMatrix;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private BitmapFont defaultFont;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private Texture playButtonTexture;
    private Texture settingsButtonTexture;
    private Texture achievementsButtonTexture;
    private Texture purchaseButtonTexture;
    private Texture soundButtonTexture;
    private Texture vibrationButtonTexture;
    private Texture soundButtonOffTexture;
    private Texture vibrationButtonOffTexture;
    private DistanceJointDef distanceJointDef = new DistanceJointDef();
    private PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
    private RopeJointDef ropeJointDef = new RopeJointDef();
    private ArrayList<Joint> joints = new ArrayList<Joint>();
    private ArrayList<Body> buttonBodies = new ArrayList<Body>();
    private ArrayList<Button> buttons = new ArrayList<Button>();
    private ArrayList<Body> platformBodies = new ArrayList<Body>();
    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private int pressedButton = -1;
    private boolean settings = false;
    private Filter noCollisionFilter;

    public MainMenu(final Screens gam) {
        game = gam;
        PIXELS_TO_METRES = 100;
        Gdx.input.setInputProcessor(this);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        game.assetManager.finishLoading();
        defaultFont = game.assetManager.get("Roboto-Medium.ttf", BitmapFont.class);
        achievementsButtonTexture = game.assetManager.get("AchievementsButton512.png", Texture.class);
        playButtonTexture = game.assetManager.get("PlayButton512.png", Texture.class);
        purchaseButtonTexture = game.assetManager.get("PurchaseButton512.png", Texture.class);
        settingsButtonTexture = game.assetManager.get("SettingsButton512.png", Texture.class);
        soundButtonTexture = game.assetManager.get("SoundButton512.png", Texture.class);
        soundButtonOffTexture = game.assetManager.get("SoundButtonOff512.png", Texture.class);
        vibrationButtonTexture = game.assetManager.get("VibrationButton512.png", Texture.class);
        vibrationButtonOffTexture = game.assetManager.get("VibrationButtonOff512.png", Texture.class);
    }

    public void create() {

    }

    public void show() {

    }

    public void render(float delta) {
        // main drawing
        camera.update();
        world.step(1 / 60f, 6, 2);
        debugMatrix = new Matrix4(camera.combined);
        debugMatrix.scale(PIXELS_TO_METRES, PIXELS_TO_METRES, 1);
        Gdx.gl.glClear(16640);
        Gdx.gl.glClearColor(150 / 255f, 40 / 255f, 27 / 255f, 1);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(238 / 255f, 238 / 255f, 238 / 255f, 1);
        shapeRenderer.rect(0, 0, screenW, screenH);
        shapeRenderer.setColor(shapeRenderer.getColor().add(-38 / 255f, -38 / 255f, -38 / 255f, 0));
        for (int i = 0; i < buttons.size(); i++) {
            Button b = buttons.get(i);
            Body d = buttonBodies.get(i);
            b.x = (PIXELS_TO_METRES * d.getPosition().x);
            b.y = (PIXELS_TO_METRES * d.getPosition().y);
            float x = (float) b.x;
            float y = (float) b.y;
            shapeRenderer.circle(x + game.shadowX, y - game.shadowX, b.radius);
        }
        shapeRenderer.end();
        buttons.get(4).texture = game.prefs.getBoolean("soundOn", true) ? soundButtonTexture : soundButtonOffTexture;
        buttons.get(5).texture = game.prefs.getBoolean("vibrationOn", true) ? vibrationButtonTexture : vibrationButtonOffTexture;
        spriteBatch.begin();
        // font
        // buttons
        for (int i = 0; i < buttons.size(); i++) {
            Button b = buttons.get(i);
            Body d = buttonBodies.get(i);
            float x = (float) b.x * Gdx.graphics.getWidth() / screenW;
            float y = (float) b.y * Gdx.graphics.getHeight() / screenH;
            float radius = b.radius * Gdx.graphics.getWidth()/ screenW;
            b.angle = (float) (((d.getAngle() * 180)) / Math.PI) % 360;
            spriteBatch.draw(b.texture, x - radius, y - radius, radius, radius, radius * 2, radius * 2, 1, 1, b.angle, 0, 0, b.texture.getWidth(), b.texture.getHeight(), false, false);
        }
        spriteBatch.end();
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(238 / 255f, 238 / 255f, 238 / 255f, 1);
        shapeRenderer.rect(0, screenH*4/5, screenW, screenH / 5);
        shapeRenderer.setColor(150 / 255f, 40 / 2555f, 27 / 255f, 1);
        for (Platform p : platforms) {
            float x = (float) p.x;
            float y = (float) p.y;
            shapeRenderer.rect(x, y, p.width / 2, p.height / 2, p.width, p.height, 1, 1, p.angle);
        }
        shapeRenderer.end();
        //debugRenderer.render(world, debugMatrix);
        if (pressedButton > -1 && pressedButton < 4 && world.getJointCount() > 1) {
            while (world.getJointCount() > 0) {
                world.destroyJoint(joints.get(0));
                joints.remove(0);
            }
            buttonBodies.get(pressedButton).setFixedRotation(true);
            distanceJointDef.bodyA = platformBodies.get(0);
            distanceJointDef.bodyB = buttonBodies.get(pressedButton);
            distanceJointDef.length = (float) getDistance(distanceJointDef.bodyA.getPosition(), distanceJointDef.bodyB.getPosition());
            joints.add(world.createJoint(distanceJointDef));
        }
        if (buttonBodies.get(1).getPosition().y < -1 || buttonBodies.get(3).getPosition().y < -1) {
            switchScreen();
            pressedButton = -1;
        }
        //rayHandler.setCombinedMatrix(debugMatrix);
        //rayHandler.updateAndRender();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void resize(int width, int height) {
        // add anything that is one-time relative to screen size
        screenH = 1280;
        screenW = 720;
        game.clearWorld();
        game.clearFonts();
        platforms.clear();
        platformBodies.clear();
        platforms.add(new Platform(18, 1024, screenW * 19 / 20, 30));
        platforms.add(new Platform(0, 0, 18, 1280));
        platforms.add(new Platform(702, 0, 18, 1280));
        platforms.add(new Platform(18, 1264, screenW * 19 / 20, 16));
        platforms.add(new Platform(18, 213, 30, 30));
        platforms.add(new Platform(702 - 30, 213, 30, 30));
        buttons.clear();
        joints.clear();
        buttonBodies.clear();
        buttons.add(new Button(screenW/2 - 100, 640, 200, 0, playButtonTexture, 0));
        buttons.add(new Button(screenW/5 - 50, 256, 100, 0, settingsButtonTexture, 1));
        buttons.add(new Button(screenW/2 - 50, 213, 100, 0, achievementsButtonTexture, 2));
        buttons.add(new Button(screenW*4/5 - 50, 256, 100, 0, purchaseButtonTexture, 3));
        buttons.add(new Button(screenW/3 - 50, 1032, 100, 0, soundButtonTexture, 4));
        buttons.add(new Button(screenW*2/3 - 50, 1032, 100, 0, vibrationButtonTexture, 5));
        initBox2DMenu();
    }

    public void render() {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void hide() {
        this.dispose();
    }

    public void dispose() {
        //world.dispose();
        System.out.println("DISPOSING");
        shapeRenderer.dispose();
        spriteBatch.dispose();
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
        if (pressedButton == -1) {
            for (int i = 0; i < buttons.size(); i++) {
                Button b = buttons.get(i);
                Body d = buttonBodies.get(i);
                float x = (float) b.x * Gdx.graphics.getWidth() / screenW;
                float y = (float) b.y * Gdx.graphics.getHeight() / screenH;
                float radius = b.radius * Gdx.graphics.getWidth()/ screenW;
                if ((screenX < x + radius && screenX > x - radius) && (Gdx.graphics.getHeight() - screenY < y + radius && Gdx.graphics.getHeight() - screenY > y - radius)) {
                    pressedButton = b.target;
                } else {
                    d.setFixedRotation(false);
                }
            }
        }
        if ((screenX < 50 && screenY > 600)) {
            game.setScreen(new StickyGolf(game));
        }
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }

    public void initBox2DMenu() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(screenW, screenH);
        camera.position.set((float) screenW/2, (float) screenH/2, 0.0f);
        world = game.world;

        BodyDef platformBodyDef = new BodyDef();
        platformBodyDef.type = BodyDef.BodyType.StaticBody;
        platformBodyDef.bullet = true;
        PolygonShape platformShape = new PolygonShape();
        FixtureDef platformFixtureDef = new FixtureDef();
        platformFixtureDef.density = 1;
        platformFixtureDef.restitution = 0;

        for (int i = 0; i < platforms.size(); i++) {
            Platform p = platforms.get(i);
            float w = p.width / PIXELS_TO_METRES;
            float h = p.height / PIXELS_TO_METRES;
            platformBodyDef.position.set((float) p.x / PIXELS_TO_METRES + w / 2, (float) p.y / PIXELS_TO_METRES + h / 2);
            platformBodyDef.angle = (float) (p.angle * Math.PI) / 180;
            Body platformBody = world.createBody(platformBodyDef);
            platformShape.setAsBox(w / 2, h / 2, new Vector2(0, 0), 0);
            platformFixtureDef.shape = platformShape;
            platformBody.createFixture(platformFixtureDef);
            platformBody.setUserData(p);
            platformBodies.add(platformBody);
        }
        platformShape.dispose();

        BodyDef buttonBodyDef = new BodyDef();
        buttonBodyDef.type = BodyDef.BodyType.DynamicBody;
        buttonBodyDef.bullet = true;
        buttonBodyDef.fixedRotation = true;
        CircleShape buttonShape = new CircleShape();
        FixtureDef buttonFixtureDef = new FixtureDef();
        buttonFixtureDef.density = 0.8f;
        buttonFixtureDef.restitution = 0.3f;

        for (Button b : buttons) {
            buttonBodyDef.position.set(((float) (b.x + ((double) (b.radius / 2)))) / PIXELS_TO_METRES, ((float) (b.y + ((double) (b.radius / 2)))) / PIXELS_TO_METRES);
            Body buttonBody = world.createBody(buttonBodyDef);
            buttonShape.setRadius(b.radius / PIXELS_TO_METRES);
            buttonFixtureDef.shape = buttonShape;
            buttonBody.createFixture(buttonFixtureDef);
            buttonBody.setUserData(b);
            buttonBodies.add(buttonBody);
        }
        buttonShape.dispose();

        distanceJointDef.collideConnected = true;
        distanceJointDef.length = 2.4f;
        distanceJointDef.bodyA = buttonBodies.get(1);
        distanceJointDef.bodyB = buttonBodies.get(2);
        joints.add(world.createJoint(distanceJointDef));
        distanceJointDef.bodyA = buttonBodies.get(2);
        distanceJointDef.bodyB = buttonBodies.get(3);
        joints.add(world.createJoint(distanceJointDef));
        distanceJointDef.length = 3.6f;
        distanceJointDef.bodyA = platformBodies.get(0);
        distanceJointDef.bodyB = buttonBodies.get(0);
        joints.add(world.createJoint(distanceJointDef));
        distanceJointDef.length = 4;
        distanceJointDef.bodyA = buttonBodies.get(0);
        distanceJointDef.bodyB = buttonBodies.get(1);
        joints.add(world.createJoint(distanceJointDef));
        distanceJointDef.bodyA = buttonBodies.get(0);
        distanceJointDef.bodyB = buttonBodies.get(2);
        joints.add(world.createJoint(distanceJointDef));
        distanceJointDef.bodyA = buttonBodies.get(0);
        distanceJointDef.bodyB = buttonBodies.get(3);
        joints.add(world.createJoint(distanceJointDef));

/*
        weldJointDef.collideConnected = true;
        weldJointDef.bodyA = buttonBodies.get(1);
        weldJointDef.bodyB = buttonBodies.get(2);
        world.createJoint(weldJointDef);
        weldJointDef.bodyA = buttonBodies.get(2);
        weldJointDef.bodyB = buttonBodies.get(3);
        world.createJoint(weldJointDef);
        weldJointDef.bodyA = platformBodies.get(0);
        weldJointDef.bodyB = buttonBodies.get(0);
        world.createJoint(weldJointDef);
        weldJointDef.bodyA = buttonBodies.get(0);
        weldJointDef.bodyB = buttonBodies.get(1);
        world.createJoint(weldJointDef);
        weldJointDef.bodyA = buttonBodies.get(0);
        weldJointDef.bodyB = buttonBodies.get(2);
        world.createJoint(weldJointDef);
        weldJointDef.bodyA = buttonBodies.get(0);
        weldJointDef.bodyB = buttonBodies.get(3);
        world.createJoint(weldJointDef);
*/

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void switchScreen() {
        switch (pressedButton) {
            case 0:
                //play
                System.out.println("GO GO GO");
                game.setScreen(new StickyGolf(game));
                break;
            case 1:
                //settings
                if (settings) {
                    game.setScreen(new MainMenu(game));
                } else {
                    settings = true;
                    buttonBodies.get(4).setFixedRotation(true);
                    buttonBodies.get(5).setFixedRotation(true);
                    ropeJointDef.maxLength = 4;
                    ropeJointDef.collideConnected = false;
                    ropeJointDef.bodyA = platformBodies.get(0);
                    ropeJointDef.bodyB = buttonBodies.get(4);
                    ropeJointDef.localAnchorA.set(new Vector2(-screenW / (6 * PIXELS_TO_METRES), 0));
                    ropeJointDef.localAnchorB.set(new Vector2(0, 0));
                    joints.add(world.createJoint(ropeJointDef));
                    ropeJointDef.bodyB = buttonBodies.get(5);
                    ropeJointDef.localAnchorA.set(new Vector2(screenW / (6 * PIXELS_TO_METRES), 0));
                    joints.add(world.createJoint(ropeJointDef));
                }
                break;
            case 2:
                //achievements
                game.setScreen(new StickyGolf(game));
                break;
            case 3:
                //premium
                game.setScreen(new StickyGolf(game));
                break;
            case 4:
                //sound
                game.prefs.putBoolean("soundOn", !game.prefs.getBoolean("soundOn", true));
                game.prefs.flush();
                break;
            case 5:
                //vibration
                game.prefs.putBoolean("vibrationOn", !game.prefs.getBoolean("vibrationOn", true));
                game.prefs.flush();
                break;
            default:
                break;
        }
    }

    public double getDistance(Vector2 obj1, Vector2 obj2) {
        return Math.sqrt(Math.pow((obj1.x - obj2.x), 2) + Math.pow((obj1.y - obj2.y), 2));
    }
}
