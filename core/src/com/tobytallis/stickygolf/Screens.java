package com.tobytallis.stickygolf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;

public class Screens extends Game {

    // any variables to carry across classes, add here as public variables
    // that are then called from the classes using game.variable_name

    public int currLevel = 0;
    public Preferences prefs;
    public int shadowX = 36;
    public int shadowY = 32;

    public void create() {
        prefs = Gdx.app.getPreferences("Settings");
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

    }
}