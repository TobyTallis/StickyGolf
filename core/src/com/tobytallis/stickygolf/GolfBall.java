package com.tobytallis.stickygolf;

public class GolfBall {

    int airHits;
    int hits;
    int blocked;
    int radius;
    int maxAirHits;
    double powerMultiplier;
    int view;
    float x;
    float y;
    float angle;
    boolean cameraFollow;

    public GolfBall() {
        blocked = 0;
        maxAirHits = 2;
        airHits = 0;
        hits = 0;
        powerMultiplier = 0.8;
        view = 20;
        angle = 0;
        cameraFollow = true;
    }
}
