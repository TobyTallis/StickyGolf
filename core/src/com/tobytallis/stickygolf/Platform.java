package com.tobytallis.stickygolf;

public class Platform {

    boolean finalPlatform;
    boolean shadow;
    boolean isDoor;
    boolean isOpen = false;
    int height;
    int[] trees;
    int width;
    // links to action (0 is no action)
    int link;
    double x;
    double y;
    float angle;

    public Platform(double x, double y, int width, int height, float angle, boolean shadow, int[] trees, boolean isDoor, int link, boolean finalPlatform) {
        this.finalPlatform = finalPlatform;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.trees = trees;
        this.shadow = shadow;
        this.isDoor = isDoor;
        this.link = link;
    }

    public Platform(double x, double y, int width, int height) {
        this(x, y, width, height, 0, true, new int[0], false, 0, false);
    }

    public Platform(double x, double y, int width, int height, int[] trees) {
        this(x, y, width, height, 0, true, trees, false, 0, false);
    }

    public Platform(double x, double y, int width, int height, boolean shadow) {
        this(x, y, width, height, 0, shadow, new int[0], false, 0, false);
    }
}