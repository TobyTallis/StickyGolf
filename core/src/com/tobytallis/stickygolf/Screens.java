package com.tobytallis.stickygolf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Screens extends com.badlogic.gdx.Game {

    // any variables to carry across classes, add here as public variables
    // that are then called from the classes using game.variable_name

    public SpriteBatch batch;

    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MainMenu(this));
        Gdx.input.setCatchBackKey(true);
    }

    public void render() {
        super.render();
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            this.setScreen(new MainMenu(this));
        }
    }

    public void dispose() {
        batch.dispose();
    }
}