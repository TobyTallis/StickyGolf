package com.tobytallis.stickygolf;

import com.badlogic.gdx.graphics.Texture;

public class Button {

    int radius;
    float angle;
    double x;
    double y;
    Texture texture;
    int screen;

    public Button(double x, double y, int radius, float angle, Texture texture, int screen) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.angle = angle;
        this.texture = texture;
        this.screen = screen;
    }
}