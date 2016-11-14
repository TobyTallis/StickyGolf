package com.tobytallis.stickygolf;

public class Box {

    int height;
    int width;
    float angle;
    float x;
    float y;

    public Box(float x, float y, int width, int height, float angle) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
    }
}