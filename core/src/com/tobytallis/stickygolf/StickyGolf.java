package com.tobytallis.stickygolf;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Arrays;

public class StickyGolf implements ApplicationListener, InputProcessor, Screen {

    final Screens game;
    private int screenW, screenH;
    private int worldW, worldH;
    private int zoom = 100;
    private int originalZoomPoint = 0;
    private int initialZoom = 0;
    private int airHitCount = 0;
    public boolean ballTouched = false;
    public boolean zoomChange = false;
    SpriteBatch spriteBatch;
    private BitmapFont zoomFont;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private Platform[] initPlatforms = new Platform[] {new Platform(500, 1200, 300, 30, 1, new int[] {180, 3, 230}),
            new Platform(1600, 1800, 600, 30, 1), new Platform(1100, 200, 30, 600, 4),
            new Platform(3300, 600, 300, 30, 1, new int[] {50, 3, 170, 200, 1, 200}), new Platform(3300, 1000, 700, 30, 1, new int[] {550, 5, 250}),
            new Platform(2800, 1200, 100, 30, 1), new Platform(2800, 1200, 100, 30, 1),
            new Platform(2800, 200, 900, 30, 1, new int[] {300, 4, 256, 700, 2, 180}), new Platform(0, 0, 4000, 30, 1, new int[] {200, 2, 256, 800, 4, 140, 2400, 5, 230}),
            new Platform(0, 0, 30, 8000, 2), new Platform(3970, 0, 30, 8000, 4)};
    private Texture golfBallTexture;
    private Texture platformTexture;
    private Texture platformUTexture;
    private Texture platformDTexture;
    private Texture platformLTexture;
    private Texture platformRTexture;
    private Texture tree1Texture;
    private Texture tree2Texture;
    private Texture tree3Texture;
    private Texture tree4Texture;
    private Texture tree5Texture;
    private Texture chosenTreeTexture;
    GolfBall golfBall = new GolfBall();

    public StickyGolf(final Screens gam) {
        game = gam;
        Gdx.input.setInputProcessor(this);
        spriteBatch = new SpriteBatch();
        golfBallTexture = new Texture(Gdx.files.internal("GolfBall.png"));
        platformUTexture = new Texture(Gdx.files.internal("Platform128.png"));
        platformDTexture = new Texture(Gdx.files.internal("Platform128d.png"));
        platformLTexture = new Texture(Gdx.files.internal("Platform128l.png"));
        platformRTexture = new Texture(Gdx.files.internal("Platform128r.png"));
        tree1Texture = new Texture(Gdx.files.internal("Tree1.png"));
        tree2Texture = new Texture(Gdx.files.internal("Tree2.png"));
        tree3Texture = new Texture(Gdx.files.internal("Tree3.png"));
        tree4Texture = new Texture(Gdx.files.internal("Tree4.png"));
        tree5Texture = new Texture(Gdx.files.internal("Tree5.png"));
        zoomFont = new BitmapFont(Gdx.files.internal("FontyFont.fnt"));
        zoomFont.setColor(Color.WHITE);
    }

    @Override
    public void create() {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // main drawing
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        gameUpdate();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.SKY);
        shapeRenderer.rect((int) (5 - golfBall.getX() * zoom) / 100 + screenW / 2 - 30 * zoom, (int) (5 - golfBall.getY() * zoom) / 100 + screenH / 2 - 15 * zoom, 10000 * zoom / 100, 20000 * zoom / 100);
        shapeRenderer.end();
        spriteBatch.begin();
        for (Platform p : platforms) {
            // draw trees
            for (int i = 0; i < p.getTrees().length; i++) {
                if (i % 3 == 0) {
                    switch(p.getTrees()[i+1]) {
                        case 2:
                            chosenTreeTexture = tree2Texture; break;
                        case 3:
                            chosenTreeTexture = tree3Texture; break;
                        case 4:
                            chosenTreeTexture = tree4Texture; break;
                        case 5:
                            chosenTreeTexture = tree5Texture; break;
                        default:
                            chosenTreeTexture = tree1Texture; break;
                    }
                    spriteBatch.draw(chosenTreeTexture, (int) (p.getX() + p.getTrees()[i] - (p.getTrees()[i+2]/2) - golfBall.getX())*zoom / 100 + screenW / 2, (int) (p.getY() + p.getHeight() - 5 - golfBall.getY())*zoom / 100 + screenH / 2, p.getTrees()[i+2] * zoom / 100, p.getTrees()[i+2] * zoom / 100);
                }
            }
            switch(p.getOrient()) {
                case 2:
                    // right
                    platformTexture = platformRTexture; break;
                case 3:
                    // down
                    platformTexture = platformDTexture; break;
                case 4:
                    // left
                    platformTexture = platformLTexture; break;
                default:
                    // up
                    platformTexture = platformUTexture; break;
            }
            if (p.getHeight() < p.getWidth()) {
                //horizontal
                for (int i = 0; i < p.getWidth() / 60; i++) {
                    spriteBatch.draw(platformTexture, (int) (p.getX() - golfBall.getX())*zoom / 100 + screenW / 2 + i*60*zoom/100, (int) (p.getY() - golfBall.getY())*zoom / 100 + screenH / 2, 65 * zoom / 100, p.getHeight() * zoom / 100);
                }
                if (p.getWidth() % 60 != 0) {
                    spriteBatch.draw(platformTexture, (int) (p.getX() - golfBall.getX())*zoom / 100 + screenW / 2 + ((p.getWidth() - (p.getWidth() % 60)) * zoom/100), (int) (p.getY() - golfBall.getY())*zoom / 100 + screenH / 2, p.getWidth() % 60 * zoom / 100, p.getHeight() * zoom / 100);
                }
            }
            else if (p.getWidth() < p.getHeight()) {
                // vertical
                for (int i = 0; i < p.getHeight() / 60; i++) {
                    spriteBatch.draw(platformTexture, (int) (p.getX() - golfBall.getX())*zoom / 100 + screenW / 2, (int) (p.getY() - golfBall.getY())*zoom / 100 + screenH / 2 + i*60*zoom/100, p.getWidth() * zoom / 100, 65 * zoom / 100);
                }
                if (p.getHeight() % 60 != 0) {
                    spriteBatch.draw(platformTexture, (int) (p.getX() - golfBall.getX())*zoom / 100 + screenW / 2, (int) (p.getY() - golfBall.getY()) * zoom / 100 + screenH / 2 + ((p.getHeight() - (p.getHeight() % 60)) * zoom / 100), p.getWidth() * zoom / 100, p.getHeight() % 60 * zoom / 100);
                }
            }
        }
        spriteBatch.draw(golfBallTexture, screenW / 2 - (golfBall.getWidth() / 2)*zoom/100, screenH / 2 - (golfBall.getHeight() / 2)*zoom/100, golfBall.getWidth()*zoom/100, golfBall.getHeight()*zoom/100);
        if (ballTouched) {
            for (int i = 1; i < golfBall.getView(); i++) {
                int x = (int) (golfBall.getDX() * i/10);
                int y = (int) (golfBall.getDY() * i/10 + (i*i - i)*0.4);
                spriteBatch.draw(golfBallTexture, screenW/2 - (x)*zoom/100, screenH/2 - (y)*zoom/100, zoom/10, zoom/10);
            }
        }
        zoomFont.draw(spriteBatch, "" + zoom, screenW - 100, screenH - 20);
        zoomFont.draw(spriteBatch, "B: " + golfBall.getBlocked(), screenW - 130, screenH - 60);
        zoomFont.draw(spriteBatch, "Ang: " + (int) Math.toDegrees(golfBall.getAngle()), screenW - 190, screenH - 100);
        zoomFont.draw(spriteBatch, "Pow: " + (int) golfBall.getPower(), screenW - 210, screenH - 140);
        zoomFont.draw(spriteBatch, "X: " + (int) golfBall.getX(), screenW - 130, screenH - 180);
        zoomFont.draw(spriteBatch, "Y: " + (int) golfBall.getY(), screenW - 130, screenH - 220);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        // add anything that is one-time relative to screen size
        screenH = height;
        screenW = width;
        worldH = 8000;
        worldW = 4000;
        golfBall.setX(300);
        golfBall.setY(400);
        golfBall.setWidth(screenW / 20);
        golfBall.setHeight(screenW / 20);
        golfBall.setMoving(true);
        golfBall.setDY(1);
        platforms.addAll(Arrays.asList(initPlatforms));
    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX > screenW/2-50 && screenX < screenW/2+50 && screenH-screenY > screenH/2-50 && screenH-screenY < screenH/2+50) {
            // if touch within golf ball boundaries
            ballTouched = true;
            golfBall.setPower(0);
            golfBall.setTime(Gdx.graphics.getDeltaTime());
        }
        if (screenX > screenW*3/4) {
            // if touch in zoom change area
            zoomChange = true;
            originalZoomPoint = screenY;
            initialZoom = zoom;
        } else {
            zoomChange = false;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (ballTouched && !golfBall.getMoving()) {
            // golf ball just hit from standing
            if (golfBall.getBlocked() == 4 && (Math.toDegrees(golfBall.getAngle()) < golfBall.getBlockedMin() || Math.toDegrees(golfBall.getAngle()) > golfBall.getBlockedMax())) {
                golfBall.setMoving(true);
            } else if (golfBall.getBlocked() != 4 && (Math.toDegrees(golfBall.getAngle()) < golfBall.getBlockedMax() && Math.toDegrees(golfBall.getAngle()) > golfBall.getBlockedMin())) {
                golfBall.setMoving(true);
            }
            // reset air hits for each standing hit
            airHitCount = 0;
        } else if (golfBall.getMoving() && golfBall.getMaxAirHits() > airHitCount) {
            // golf ball hit from mid air
            golfBall.setDX(golfBall.getDX() + Math.cos(golfBall.getAngle()) * golfBall.getPower() / 5);
            golfBall.setDY(golfBall.getDY() + Math.sin(golfBall.getAngle()) * golfBall.getPower() / 5);
            airHitCount += 1;
        }
        ballTouched = false;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (ballTouched) {
            // set power and angle based on distance from golf ball
            golfBall.setPower(golfBall.getPowerMultiplier()*(Math.sqrt((screenX - screenW / 2) * (screenX - screenW / 2) + ((screenH - screenY) - screenH / 2) * ((screenH - screenY) - screenH / 2))));
            golfBall.setAngle(Math.atan2(((screenH - screenY) - screenH / 2), (screenX - screenW / 2)));
            if (!golfBall.getMoving()) {
                // initial DX and DY
                golfBall.setDX(Math.cos(golfBall.getAngle()) * golfBall.getPower());
                golfBall.setDY(Math.sin(golfBall.getAngle()) * golfBall.getPower());
            }
        } else if (zoomChange) {
            zoom = initialZoom + (screenY - originalZoomPoint) / 2;
            if (zoom < 45) {
                originalZoomPoint = screenY;
                initialZoom = 45;
                zoom = 45;
            } else if (zoom > 100) {
                originalZoomPoint = screenY;
                initialZoom = 100;
                zoom = 100;
            }
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void gameUpdate() {
        if (golfBall.getMoving()) {
            golfBall.setX(golfBall.getX() - golfBall.getDX() / 10);
            golfBall.setY(golfBall.getY() - golfBall.getDY() / 10);
            for (Platform p : platforms) {
                /* if (golfBall.getX() > p.getX() && golfBall.getX() < p.getX()+p.getWidth() && golfBall.getY() > p.getY() && golfBall.getY() < p.getY()+p.getHeight()) {
                    golfBall.setMoving(false);
                    if (golfBall.getX() + golfBall.getDX() / 10 > p.getX() && golfBall.getX() + golfBall.getDX() / 10 < p.getX() + p.getWidth()) {
                        if (golfBall.getY() < p.getY() + p.getHeight() / 2) {
                            golfBall.setY(p.getY());
                        } else {
                            golfBall.setY(p.getY() + p.getHeight());
                        }
                    } else if (golfBall.getY() + golfBall.getDY() / 10 > p.getY() && golfBall.getY() + golfBall.getDY() / 10 < p.getY() + p.getHeight()) {
                        if (golfBall.getX() < p.getX() + p.getWidth() / 2) {
                            golfBall.setX(p.getX());
                        } else {
                            golfBall.setX(p.getX() + p.getWidth());
                        }
                    }
                } */
                if (p.getX() - golfBall.getX() > 0 && p.getX() - (golfBall.getX() + golfBall.getDX() / 10) < 0 || p.getX() - golfBall.getX() < 0 && p.getX() - (golfBall.getX() + golfBall.getDX() / 10) > 0 || p.getX() + p.getWidth() - golfBall.getX() > 0 && p.getX() + p.getWidth() - (golfBall.getX() + golfBall.getDX() / 10) < 0 || p.getX() + p.getWidth() - golfBall.getX() < 0 && p.getX() + p.getWidth() - (golfBall.getX() + golfBall.getDX() / 10) > 0) {
                    if (golfBall.getY() >= p.getY() && golfBall.getY() <= p.getY() + p.getHeight()) {
                        golfBall.setMoving(false);
                        if (golfBall.getX() + golfBall.getDX() / 10 < p.getX() + p.getWidth()/2) {
                            golfBall.setX(p.getX() - 1);
                            golfBall.setBlocked(2);
                        } else if (golfBall.getX() + golfBall.getDX() / 10 > p.getX() + p.getWidth()/2) {
                            golfBall.setX(p.getX() + p.getWidth() + 1);
                            golfBall.setBlocked(4);
                        }
                    }
                }
                if (p.getY() - golfBall.getY() > 0 && p.getY() - (golfBall.getY() + golfBall.getDY() / 10) < 0 || p.getY() - golfBall.getY() < 0 && p.getY() - (golfBall.getY() + golfBall.getDY() / 10) > 0 || p.getY() + p.getHeight() - golfBall.getY() > 0 && p.getY() + p.getHeight() - (golfBall.getY() + golfBall.getDY() / 10) < 0 || p.getY() + p.getHeight() - golfBall.getY() < 0 && p.getY() + p.getHeight() - (golfBall.getY() + golfBall.getDY() / 10) > 0) {
                    if (golfBall.getX() >= p.getX() && golfBall.getX() <= p.getX() + p.getWidth()) {
                        golfBall.setMoving(false);
                        if (golfBall.getY() + golfBall.getDY() / 10 < p.getY() + p.getHeight()/2) {
                            golfBall.setY(p.getY() - 1);
                            golfBall.setBlocked(1);
                        } else if (golfBall.getY() + golfBall.getDY() / 10 > p.getY() + p.getHeight()/2) {
                            golfBall.setY(p.getY() + p.getHeight() + 1);
                            golfBall.setBlocked(3);
                        }
                    }
                }
            }
            golfBall.setDY(golfBall.getDY() + 8);
            if (golfBall.getX() < 30) {
                // ACHIEVEMENT RULEBREAKER (trying to go off edge of screen)
                golfBall.setX(30);
            } else if (golfBall.getX() > worldW - 30) {
                // ACHIEVEMENT RULEBREAKER (trying to go off edge of screen)
                golfBall.setX(worldW - 30);
            }
        }
    }
}