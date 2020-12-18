package com.example.touchdispeargame;

import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ItemData {

    private ConstraintLayout itemRoot;

    private ImageView ivIcon;

    private int id;

    private float leftX;

    private float rightX;

    private float topY;

    private float bottomY;

    private float middleX;

    private float middleY;

    public ImageView getIvIcon() {
        return ivIcon;
    }

    public void setIvIcon(ImageView ivIcon) {
        this.ivIcon = ivIcon;
    }

    public ConstraintLayout getItemRoot() {
        return itemRoot;
    }

    public void setItemRoot(ConstraintLayout itemRoot) {
        this.itemRoot = itemRoot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLeftX() {
        return leftX;
    }

    public void setLeftX(float leftX) {
        this.leftX = leftX;
    }

    public float getRightX() {
        return rightX;
    }

    public void setRightX(float rightX) {
        this.rightX = rightX;
    }

    public float getTopY() {
        return topY;
    }

    public void setTopY(float topY) {
        this.topY = topY;
    }

    public float getBottomY() {
        return bottomY;
    }

    public void setBottomY(float bottomY) {
        this.bottomY = bottomY;
    }

    public float getMiddleX() {
        return middleX;
    }

    public void setMiddleX(float middleX) {
        this.middleX = middleX;
    }

    public float getMiddleY() {
        return middleY;
    }

    public void setMiddleY(float middleY) {
        this.middleY = middleY;
    }
}
