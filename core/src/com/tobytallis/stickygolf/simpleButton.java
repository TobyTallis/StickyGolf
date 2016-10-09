package com.tobytallis.stickygolf;

import com.badlogic.gdx.graphics.Texture;

public class simpleButton {

    int angle;
    int height;
    boolean rotation;
    Texture texture;
    int width;
    double x;
    double y;

    public simpleButton(Texture tex, double startX, double startY, int startWidth, int startHeight, int startAngle) {
        rotation = false;
        texture = tex;
        x = startX;
        y = startY;
        width = startWidth;
        height = startHeight;
        angle = startAngle;
    }
}
