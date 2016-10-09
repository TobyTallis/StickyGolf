package com.tobytallis.stickygolf;

public class Text {

    int font;
    String text;
    int x;
    int y;
    MovingPlatform platformToFollow;
    Box boxToFollow;

    public Text(int font, String text, int x, int y, MovingPlatform platformToFollow, Box boxToFollow) {
        this.font = font;
        this.text = text;
        this.x = x;
        this.y = y;
        this.platformToFollow = platformToFollow;
    }

    public Text(int font, String text, int x, int y, MovingPlatform platformToFollow) {
        this(font, text, x, y, platformToFollow, null);
    }

    public Text(int font, String text, int x, int y, Box boxToFollow) {
        this(font, text, x, y, null, boxToFollow);
    }

    public Text(int font, String text, int x, int y) {
        this(font, text, x, y, null, null);
    }
}
