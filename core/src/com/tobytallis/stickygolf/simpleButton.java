package com.tobytallis.stickygolf;

import com.badlogic.gdx.graphics.Texture;

public class simpleButton {

    private Texture texture;
    private double x;
    private double y;
    private int width;
    private int height;
    private int angle;
    private boolean rotation = false;

    public simpleButton(Texture tex, double startX, double startY, int startWidth, int startHeight, int startAngle) {
        texture = tex;
        x = startX;
        y = startY;
        width = startWidth;
        height = startHeight;
        angle = startAngle;
    }

    public void setTexture(Texture newTexture) {
        texture = newTexture;
    }

    public void setX(double newX) {
        x = newX;
    }

    public void setY(double newY) {
        y = newY;
    }

    public void setWidth(int newWidth) {
        width = newWidth;
    }

    public void setHeight(int newHeight) {
        height = newHeight;
    }

    public void setAngle(int newAngle) {
        angle = newAngle;
    }

    public void setRotation(boolean newRotation) {
        rotation = newRotation;
    }

    public Texture getTexture() {
        return texture;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getAngle() {
        return angle;
    }

    public boolean getRotation() {
        return rotation;
    }
}
