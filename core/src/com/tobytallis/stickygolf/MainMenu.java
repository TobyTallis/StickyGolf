package com.tobytallis.stickygolf;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.awt.Button;
import java.util.ArrayList;

public class MainMenu implements ApplicationListener, InputProcessor, Screen {

    public class buttonActor extends Actor {

    }

    final Screens game;
    private int screenW, screenH;
    SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private simpleButton playButton;
    private ArrayList<simpleButton> buttons = new ArrayList<simpleButton>();

    public MainMenu(final Screens gam) {
        game = gam;
        Gdx.input.setInputProcessor(this);
        spriteBatch = new SpriteBatch();
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
        spriteBatch.begin();
        for (simpleButton b : buttons) {
            if (b.getRotation()) {
                b.setAngle((int) (b.getAngle()+delta*75));
            }
            spriteBatch.draw(b.getTexture(), (int) b.getX(), (int) b.getY(), b.getWidth(), 0, b.getWidth(), b.getHeight(), 1, 1, b.getAngle(), 0, 0, b.getTexture().getWidth(), b.getTexture().getHeight(), false, false);
            if (b.getAngle() > 90) {
                game.setScreen(new StickyGolf(game));
            }
        }
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        // add anything that is one-time relative to screen size
        screenH = height;
        screenW = width;
        playButton = new simpleButton(new Texture(Gdx.files.internal("PlayButton.png")), screenW/4, screenH/2, screenW/2, screenW/4, 0);
        buttons.add(playButton);
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
        for (simpleButton b: buttons) {
            if (screenX > b.getX() && screenX < b.getX() + b.getWidth() && screenH - screenY > b.getY() && screenH - screenY < b.getY() + b.getHeight()) {
                b.setRotation(true);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
