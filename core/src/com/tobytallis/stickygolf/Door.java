package com.tobytallis.stickygolf;

import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;

public class Door {

    boolean shadow;
    boolean isOpen = false;
    boolean justChanged = false;
    int height;
    int width;
    // links to action (0 is no action)
    int link;
    double x;
    double y;
    double openX;
    double openY;
    float angle;
    PrismaticJoint joint;

    public Door(double x, double y, double openX, double openY, int width, int height, float angle, int link, boolean shadow) {
        this.x = x;
        this.y = y;
        this.openX = openX;
        this.openY = openY;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.link = link;
        this.shadow = shadow;
    }

    public Door(double x, double y, double openX, double openY, int width, int height, int link) {
        this(x, y, openX, openY, width, height, 0, link, true);
    }
}