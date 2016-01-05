package com.tobytallis.stickygolf;

public class GolfBall {

    // Texture UPGRADE?
    private double x;
    private double y;
    private double dx;
    private double dy;
    private int width;
    private int height;
    private double power;
    private double angle;
    private boolean moving;
    private int blocked = 0;
    private int blockedMin;
    private int blockedMax;
    // UPGRADE?
    private int maxAirHits = 2;
    private double time;
    // UPGRADE?
    private double powerMultiplier = 0.8;
    // UPGRADE?
    private int view = 20;

    public GolfBall() {

    }

    public void setX(double newX) {
        x = newX;
    }

    public void setY(double newY) {
        y = newY;
    }

    public void setDX(double newDX) {
        dx = newDX;
    }

    public void setDY(double newDY) {
        dy = newDY;
    }

    public void setWidth(int newWidth) {
        width = newWidth;
    }

    public void setHeight(int newHeight) {
        height = newHeight;
    }

    public void setPower(double newPower) {
        power = newPower;
    }

    public void setAngle(double newAngle) {
        angle = newAngle;
    }

    public void setMoving(boolean newMoving) {
        moving = newMoving;
    }

    public void setBlocked(int newBlocked) {
        blocked = newBlocked;
        switch (blocked) {
            case 3:
                blockedMin = -179;
                blockedMax = -1;
                break;
            case 2:
                blockedMin = -89;
                blockedMax = 89;
                break;
            case 4:
                blockedMin = -91;
                blockedMax = 91;
                break;
            case 1:
                blockedMin = 1;
                blockedMax = 179;
                break;
        }
    }

    public void setMaxAirHits(int newMaxAirHits) {
        maxAirHits = newMaxAirHits;
    }

    public void setTime(double newTime) {
        time = newTime;
    }

    public void setPowerMultiplier(double newPowerMultiplier) {
        powerMultiplier = newPowerMultiplier;
    }

    public void setView(int newView) {
        view = newView;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDX() {
        return dx;
    }

    public double getDY() {
        return dy;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getPower() {
        return power;
    }

    public double getAngle() {
        return angle;
    }

    public boolean getMoving() {
        return moving;
    }

    public int getBlockedMin() {
        return blockedMin;
    }

    public int getBlockedMax() {
        return blockedMax;
    }

    public int getBlocked() {
        return blocked;
    }

    public int getMaxAirHits() {
        return maxAirHits;
    }

    public double getTime() {
        return time;
    }

    public double getPowerMultiplier() {
        return powerMultiplier;
    }

    public int getView() {
        return view;
    }
}
