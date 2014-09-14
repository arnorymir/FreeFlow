package com.example.flowflow.freeflow;


import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by bjornorri on 13/09/14.
 */
public class Puzzle {

    private int mSize;
    private int mNumColors;
    private Dot[] mDots;

    public Puzzle(int size, int numColors, Dot[] dots) {
        mSize = size;
        mNumColors = numColors;
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

}