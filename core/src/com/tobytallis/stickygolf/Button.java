package com.tobytallis.stickygolf;

import com.badlogic.gdx.graphics.Texture;

public class Button {

    int radius;
    float angle;
    float x;
    float y;
    Texture texture;
    int target;
    boolean enabled;

    public Button(float x, float y, int radius, float angle, Texture texture, int target, boolean enabled) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.angle = angle;
        this.texture = texture;
        this.target = target;
        this.enabled = enabled;
    }

    public Button(float x, float y, int radius, float angle, Texture texture, int target) {
        this(x, y, radius, angle, texture, target, true);
    }
}