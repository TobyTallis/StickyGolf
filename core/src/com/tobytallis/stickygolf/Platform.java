package com.tobytallis.stickygolf;

public class Platform {

    private double x;
    private double y;
    private int width;
    private int height;
    private int orient;
    private int[] trees;
    private boolean finalPlatform = false;

    public Platform(double startX, double startY, int startWidth, int startHeight, int orientation, int[] treeList) {
        x = startX;
        y = startY;
        width = startWidth;
        height = startHeight;
        // orientation format  1 = up, 2 = right, 3 = down, 4 = left
        orient = orientation;
        // treeList should be in format {x position from platform start, type of tree (1-5), size of tree (256 max probably)}
        trees = treeList;
    }

    public Platform(double startX, double startY, int startWidth, int startHeight, int orientation) {
        this(startX, startY, startWidth, startHeight, orientation, new int[] {});
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

    public void setOrient(int newOrient) {
        orient = newOrient;
    }

    public void setTrees(int[] newTrees) {
        trees = newTrees;
    }

    public void setFinalPlatform(boolean newFinalPlatform) {
        finalPlatform = newFinalPlatform;
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

    public int getOrient() {
        return orient;
    }

    public int[] getTrees() {
        return trees;
    }

    public boolean getFinalPlatform() {
        return finalPlatform;
    }
}
