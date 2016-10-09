package com.tobytallis.stickygolf;

public class Level {

    int world;
    int width;
    int height;
    int startX;
    int startY;
    Platform[] platforms;
    Box[] boxes;
    Switch[] switches;
    Door[] doors;
    MovingPlatform[] movingPlatforms;
    Text[] texts;

    public Level(int world, int width, int height, int startX, int startY, Platform[] platforms, Box[] boxes, Switch[] switches, Door[] doors, MovingPlatform[] movingPlatforms, Text[] texts) {
        this.world = world;
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
        this.platforms = platforms;
        this.boxes = boxes;
        this.switches = switches;
        this.doors = doors;
        this.movingPlatforms = movingPlatforms;
        this.texts = texts;
    }
}
