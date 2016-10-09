package com.tobytallis.stickygolf;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import java.util.ArrayList;

public class MainMenu implements ApplicationListener, InputProcessor, Screen {

    final Screens game;
    private int screenW, screenH;

    //ArrayList<PointLight> lights = new ArrayList<PointLight>();
    //RayHandler rayHandler;
    World world;
    final float PIXELS_TO_METRES;
    Matrix4 debugMatrix;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    SpriteBatch spriteBatch;
    private Texture playButtonTexture;
    private Texture settingsButtonTexture;
    private Texture achievementsButtonTexture;
    private Texture purchaseButtonTexture;
    DistanceJointDef distanceJointDef = new DistanceJointDef();
    PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
    private ArrayList<Joint> joints = new ArrayList<Joint>();
    private ArrayList<Body> buttonBodies = new ArrayList<Body>();
    private ArrayList<Button> buttons = new ArrayList<Button>();
    private ArrayList<Body> platformBodies = new ArrayList<Body>();
    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private int pressedButton = -1;

    public MainMenu(final Screens gam) {
        PIXELS_TO_METRES = 100;
        game = gam;
        Gdx.input.setInputProcessor(this);
        spriteBatch = new SpriteBatch();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Medium.ttf"));
        parameter.size = 30;
        font = fontGenerator.generateFont(parameter);
        font.setColor(Color.WHITE);
        shapeRenderer = new ShapeRenderer();
        playButtonTexture = new Texture(Gdx.files.internal("PlayButton.png"));
        settingsButtonTexture = new Texture(Gdx.files.internal("SettingsButton.png"));
        achievementsButtonTexture = new Texture(Gdx.files.internal("AchievementsButton.png"));
        purchaseButtonTexture = new Texture(Gdx.files.internal("PurchaseButton.png"));
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
        debugMatrix.scale(100.0f, 100.0f, 1.0f);
        Gdx.gl.glClear(16640);
        Gdx.gl.glClearColor(150 / 255f, 40 / 255f, 27 / 255f, 1);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(238 / 255f, 238 / 255f, 238 / 255f, 1);
        shapeRenderer.rect(0, 0, screenW, screenH);
        shapeRenderer.setColor(shapeRenderer.getColor().add(-38 / 255f, -38 / 255f, -38 / 255f, 0));
        for (int i = 0; i < buttons.size(); i++) {
            Button b = buttons.get(i);
            Body d = buttonBodies.get(i);
            b.x = (double) (PIXELS_TO_METRES * d.getPosition().x);
            b.y = (double) (PIXELS_TO_METRES * d.getPosition().y);
            float x = (float) b.x;
            float y = (float) b.y;
            shapeRenderer.circle(x + screenW / 20, y - screenH / 40, b.radius);
        }
        shapeRenderer.setColor(150 / 255f, 40 / 2555f, 27 / 255f, 1);
        for (Platform p : platforms) {
            float x = (float) p.x;
            float y = (float) p.y;
            shapeRenderer.rect(x, y, p.width / 2, p.height / 2, p.width, p.height, 1, 1, p.angle);
        }
        shapeRenderer.end();
        spriteBatch.begin();
        // font
        // buttons
        for (int i = 0; i < buttons.size(); i++) {
            Button b = buttons.get(i);
            Body d = buttonBodies.get(i);
            float x = (float) b.x;
            float y = (float) b.y;
            b.angle = (float) (((d.getAngle() * 180)) / Math.PI) % 360;
            spriteBatch.draw(b.texture, x - b.radius, y - b.radius, b.radius, b.radius, b.radius * 2, b.radius * 2, 1, 1, b.angle, 0, 0, 512, 512, false, false);
        }
        spriteBatch.end();
        //debugRenderer.render(world, debugMatrix);
        if (pressedButton != -1 && world.getJointCount() > 1) {
            while (world.getJointCount() > 0) {
                world.destroyJoint(joints.get(0));
                joints.remove(0);
            }
            distanceJointDef.bodyA = buttonBodies.get(pressedButton);
            distanceJointDef.bodyB = platformBodies.get(0);
            distanceJointDef.length = (float) getDistance(distanceJointDef.bodyA.getPosition(), distanceJointDef.bodyB.getPosition());
            joints.add(world.createJoint(distanceJointDef));
        }
        if (buttonBodies.get(1).getPosition().y < 0 || buttonBodies.get(3).getPosition().y < 0) {
            switchScreen();
        }
        //rayHandler.setCombinedMatrix(debugMatrix);
        //rayHandler.updateAndRender();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void resize(int width, int height) {
        // add anything that is one-time relative to screen size
        screenH = 1280;
        screenW = 720;
        platforms.clear();
        platformBodies.clear();
        platforms.add(new Platform(screenW / 40, screenH*4/5, screenW * 19 / 20, 30));
        platforms.add(new Platform(0, 0, screenW / 40, screenH));
        platforms.add(new Platform(screenW * 39 / 40, 0, screenW / 40, screenH));
        platforms.add(new Platform(screenW / 40, screenH * 79 / 80, screenW * 19 / 20, screenH / 80));
        platforms.add(new Platform(screenW / 40, screenH / 6, 30, 30));
        platforms.add(new Platform(screenW * 39 / 40 - 30, screenH / 6, 30, 30));
        buttons.clear();
        joints.clear();
        buttonBodies.clear();
        buttons.add(new Button(screenW/2 - 100, screenH/2, 200, 0, playButtonTexture, 0));
        buttons.add(new Button(screenW/5 - 50, screenH/5, 100, 0, settingsButtonTexture, 1));
        buttons.add(new Button(screenW/2 - 50, screenH/6, 100, 0, achievementsButtonTexture, 2));
        buttons.add(new Button(screenW*4/5 - 50, screenH/5, 100, 0, purchaseButtonTexture, 3));
        initBox2DMenu();
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
        world.dispose();
        System.out.println("DISPOSING");
        shapeRenderer.dispose();
        spriteBatch.dispose();
        font.dispose();
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
            for (Button b : buttons) {
                if ((screenX < b.x + b.radius && screenX > b.x - b.radius) && (screenH - screenY < b.y + b.radius && screenH - screenY > b.y - b.radius)) {
                    pressedButton = b.screen;
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
        world = new World(new Vector2(0, -10), true);

        BodyDef platformBodyDef = new BodyDef();
        platformBodyDef.type = BodyDef.BodyType.StaticBody;
        platformBodyDef.bullet = true;
        PolygonShape platformShape = new PolygonShape();
        FixtureDef platformFixtureDef = new FixtureDef();
        platformFixtureDef.density = 1;
        platformFixtureDef.restitution = 0;
/*        for (Platform p : platforms) {
            platformBodyDef.position.set(((float) (p.x + ((double) (p.width / 2)))) / PIXELS_TO_METRES, ((float) (p.y + ((double) (p.height / 2)))) / PIXELS_TO_METRES);
            Body platformBody = world.createBody(platformBodyDef);
            platformShape.setAsBox(((float) (p.width / 2)) / PIXELS_TO_METRES, ((float) (p.height / 2)) / PIXELS_TO_METRES);
            platformFixtureDef.shape = platformShape;
            platformBody.createFixture(platformFixtureDef);
            platformBody.setUserData(p);
            platformBodies.add(platformBody);
        }*/
        for (int i = 0; i < platforms.size(); i++) {
            Platform p = platforms.get(i);
            float w = p.width / PIXELS_TO_METRES;
            float h = p.height / PIXELS_TO_METRES;
            platformBodyDef.position.set((float) p.x / PIXELS_TO_METRES + w / 2, (float) p.y / PIXELS_TO_METRES + h / 2);
            platformBodyDef.angle = (float) (p.angle * Math.PI) / 180;
            Body platformBody = world.createBody(platformBodyDef);
            platformShape.setAsBox(w / 2, h / 2, new Vector2(0, 0), 0);
            platformFixtureDef.shape = platformShape;
            //if (platforms.size() - i <= 4) {
            //    platformFixtureDef.filter.maskBits = 0x0001 | 0x0002 | 0x0004;
            //    platformBody.createFixture(platformFixtureDef);
            //} else {
            //    platformFixtureDef.filter.maskBits = -1;
            platformBody.createFixture(platformFixtureDef);
            //}
            platformBody.setUserData(p);
            platformBodies.add(platformBody);
        }
        platformShape.dispose();

        BodyDef buttonBodyDef = new BodyDef();
        buttonBodyDef.type = BodyDef.BodyType.DynamicBody;
        buttonBodyDef.bullet = true;
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

        prismaticJointDef.bodyA = buttonBodies.get(0);
        prismaticJointDef.bodyB = platformBodies.get(0);
        PrismaticJoint pJoint = (PrismaticJoint) world.createJoint(prismaticJointDef);

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
                game.setScreen(new StickyGolf(game));
                break;
            case 1:
                //settings
                game.setScreen(new StickyGolf(game));
                break;
            case 2:
                //achievements
                game.setScreen(new StickyGolf(game));
                break;
            case 3:
                //premium
                game.setScreen(new StickyGolf(game));
                break;
            default:
                break;
        }
    }

    public double getDistance(Vector2 obj1, Vector2 obj2) {
        return Math.sqrt(Math.pow((obj1.x - obj2.x), 2) + Math.pow((obj1.y - obj2.y), 2));
    }
}
