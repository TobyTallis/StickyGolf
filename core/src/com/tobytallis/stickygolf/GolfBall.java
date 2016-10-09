package com.tobytallis.stickygolf;

public class GolfBall {

    int airHits;
    int hits;
    int blocked;
    int height;
    int maxAirHits;
    double powerMultiplier;
    int view;
    int width;
    double x;
    double y;
    boolean cameraFollow;

    public GolfBall() {
        blocked = 0;
        maxAirHits = 2;
        airHits = 0;
        hits = 0;
        powerMultiplier = 0.8;
        view = 20;
        cameraFollow = true;
    }
}
