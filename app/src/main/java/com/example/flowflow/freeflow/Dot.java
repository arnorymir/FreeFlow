package com.example.flowflow.freeflow;

/**
 * Created by bjornorri on 13/09/14.
 */
public class Dot {

    private Coordinate mCell;
    private int mColorID;

    public Dot(Coordinate cell, int colorID) {
        mCell = cell;
        mColorID = colorID;
    }

    public Coordinate getCell() {
        return mCell;
    }

    public int getColorID() {
        return mColorID;
    }
}
