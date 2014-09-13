package com.example.flowflow.freeflow;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by bjornorri on 13/09/14.
 */
public class Puzzle {

    private int mSize;
    private int mNumColors;

    // Each color is assigned an integer. The map contains the dots for each color.
    private Map<Integer, Coordinate[]> mDots;

    // Constructor is given the size, number of colors and the locations of the dots.
    // The dots come in pairs, so dots 1 and 2 belong to one color, 3 and 4 to another color etc...
    public Puzzle(int size, Coordinate[] dots) {
        mSize = size;
        mNumColors = dots.length / 2;
        for (int i = 0; i < mNumColors; i += 2) {
            int colorNo = i / 2;
            mDots.put(colorNo, Arrays.copyOfRange(dots, i, i + 1));
        }
    }

    public int getSize() {
        return mSize;
    }

    public int getNumColors() {
        return mNumColors;
    }
}
