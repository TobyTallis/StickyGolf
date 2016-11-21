package com.tobytallis.stickygolf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Screens extends Game {

    // any variables to carry across classes, add here as public variables
    // that are then called from the classes using game.variable_name

    public int currLevel = 0;
    public Preferences prefs;
    public int shadowX = 36;
    public int shadowY = 32;
    public AssetManager assetManager;
    public World world = new World(new Vector2(0, -10), true);

    public void create() {
        prefs = Gdx.app.getPreferences("Settings");
        assetManager = new AssetManager();
        loadAssets();
        this.setScreen(new MainMenu(this));
        Gdx.input.setCatchBackKey(true);
    }

    public void render() {
        super.render();
        assetManager.update();
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            this.setScreen(new MainMenu(this));
        }
    }

    public void dispose() {

    }

    public void clearWorld() {
        if (world != null) {
            world.dispose();
        }
        world = new World(new Vector2(0, -10), true);
        System.out.println("WORLD CLEARED");
    }

    public void clearFonts() {
        System.out.println("FONTS CLEARED");
    }

    public void loadAssets() {
        TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
        textureParameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        textureParameter.magFilter = Texture.TextureFilter.MipMapLinearLinear;
        textureParameter.genMipMaps = true;
        assetManager.load("AchievementsButton512.png", Texture.class, textureParameter);
        assetManager.load("Box512.png", Texture.class, textureParameter);
        assetManager.load("Flag512.png", Texture.class, textureParameter);
        assetManager.load("GolfBall512.png", Texture.class, textureParameter);
        assetManager.load("LookButton512.png", Texture.class, textureParameter);
        assetManager.load("LookButtonOff512.png", Texture.class, textureParameter);
        assetManager.load("PlayButton512.png", Texture.class, textureParameter);
        assetManager.load("PowerUpButton512.png", Texture.class, textureParameter);
        assetManager.load("PowerUpButtonOff512.png", Texture.class, textureParameter);
        assetManager.load("PurchaseButton512.png", Texture.class, textureParameter);
        assetManager.load("RestartButton512.png", Texture.class, textureParameter);
        assetManager.load("SettingsButton512.png", Texture.class, textureParameter);
        assetManager.load("SoundButton512.png", Texture.class, textureParameter);
        assetManager.load("SoundButtonOff512.png", Texture.class, textureParameter);
        assetManager.load("VibrationButton512.png", Texture.class, textureParameter);
        assetManager.load("VibrationButtonOff512.png", Texture.class, textureParameter);
        assetManager.load("ZoomInButton512.png", Texture.class, textureParameter);
        assetManager.load("ZoomOutButton512.png", Texture.class, textureParameter);
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter thinFontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        FreetypeFontLoader.FreeTypeFontLoaderParameter defaultFontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        FreetypeFontLoader.FreeTypeFontLoaderParameter boldFontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        thinFontParameter.fontParameters.color = Color.BLACK;
        thinFontParameter.fontFileName = "Roboto-Thin.ttf";
        thinFontParameter.fontParameters.size = 25;
        thinFontParameter.fontParameters.minFilter = Texture.TextureFilter.Linear;
        thinFontParameter.fontParameters.magFilter = Texture.TextureFilter.Linear;
        assetManager.load(thinFontParameter.fontFileName, BitmapFont.class, thinFontParameter);
        defaultFontParameter.fontParameters.color = Color.BLACK;
        defaultFontParameter.fontFileName = "Roboto-Medium.ttf";
        defaultFontParameter.fontParameters.size = 30;
        defaultFontParameter.fontParameters.minFilter = Texture.TextureFilter.Linear;
        defaultFontParameter.fontParameters.magFilter = Texture.TextureFilter.Linear;
        assetManager.load(defaultFontParameter.fontFileName, BitmapFont.class, defaultFontParameter);
        boldFontParameter.fontParameters.color = Color.BLACK;
        boldFontParameter.fontFileName = "Roboto-Black.ttf";
        boldFontParameter.fontParameters.size = 40;
        boldFontParameter.fontParameters.minFilter = Texture.TextureFilter.Linear;
        boldFontParameter.fontParameters.magFilter = Texture.TextureFilter.Linear;
        assetManager.load(boldFontParameter.fontFileName, BitmapFont.class, boldFontParameter);
    }
}