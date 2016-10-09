package com.tobytallis.stickygolf;

public class Switch {

    boolean isPressed;
    boolean onOnce;
    int size;
    // links to action (0 is no action)
    int link;
    double x;
    double y;

    public Switch(double x, double y, int size, int link, boolean isPressed, boolean onOnce) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.link = link;
        this.isPressed = isPressed;
        this.onOnce = onOnce;
    }

    public Switch(double x, double y, int size, int link, boolean isPressed) {
        this(x, y, size, link, isPressed, false);
    }

    public Switch(double x, double y, int size, int link) {
        this(x, y, size, link, false, false);
    }

    public Switch(double x, double y, int size) {
        this(x, y, size, 0, false, false);
    }
}
