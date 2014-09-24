package com.example.flowflow.freeflow;


import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by bjornorri on 13/09/14.
 */
public class Puzzle {

    private int mID;
    private int mSize;
    private int mNumColors;
    private Dot[] mDots;

    public Puzzle(int ID, int size, Dot[] dots) {
        mID = ID;
        mSize = size;
        mNumColors = dots.length / 2;
        mDots = dots;
    }

    public int getSize() {
        return mSize;
    }

    public int getNumberOfColors() {
        return mNumColors;
    }

    public Dot[] getDots() {
        return mDots;
    }

    public int getID() {
        return mID;
    }

    public void setID(int id) {
        mID = id;
    }
}
