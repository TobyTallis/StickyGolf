package com.tobytallis.stickygolf;

public class Platform {

    boolean finalPlatform;
    boolean shadow;
    boolean isDoor;
    int height;
    int[] trees;
    int width;
    // links to action (0 is no action)
    int link;
    float x;
    float y;
    float angle;

    public Platform(float x, float y, int width, int height, float angle, boolean shadow, int[] trees, int link, boolean finalPlatform) {
        this.finalPlatform = finalPlatform;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.trees = trees;
        this.shadow = shadow;
        this.link = link;
    }

    public Platform(float x, float y, int width, int height) {
        this(x, y, width, height, 0, true, new int[0], 0, false);
    }

    public Platform(float x, float y, int width, int height, int[] trees) {
        this(x, y, width, height, 0, true, trees, 0, false);
    }

    public Platform(float x, float y, int width, int height, boolean shadow) {
        this(x, y, width, height, 0, shadow, new int[0], 0, false);
    }
}