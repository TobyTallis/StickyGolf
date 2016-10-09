package com.tobytallis.stickygolf;

public class MovingPlatform {

    boolean finalPlatform;
    boolean shadow;
    boolean isOpen;
    int height;
    int[] trees;
    int width;
    // links to action (0 is no action)
    int link;
    double startX;
    double startY;
    double endX;
    double endY;
    double x;
    double y;
    float angle;
    boolean isMoving;
    float time;
    int shape;

    public MovingPlatform(double startX, double startY, double endX, double endY, int width, int height, float time, float angle, int shape, boolean shadow, int[] trees, boolean isMoving, int link) {
        finalPlatform = false;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.width = width;
        this.height = height;
        this.time = time;
        this.angle = angle;
        this.trees = trees;
        this.shadow = shadow;
        this.isMoving = isMoving;
        this.link = link;
        this.shape = shape;
    }

    public MovingPlatform(double startX, double startY, double endX, double endY, int width, int height, float time) {
        this(startX, startY, endX, endY, width, height, time, 0, 1, true, new int[0], true, 0);
    }

    public MovingPlatform(double startX, double startY, double endX, double endY, int width, int height, float time, int shape) {
        this(startX, startY, endX, endY, width, height, time, 0, shape, true, new int[0], true, 0);
    }
}