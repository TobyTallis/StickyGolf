package com.tobytallis.stickygolf;

public class Box {

    int height;
    int width;
    float angle;
    double x;
    double y;

    public Box(double x, double y, int width, int height, float angle) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
    }
}