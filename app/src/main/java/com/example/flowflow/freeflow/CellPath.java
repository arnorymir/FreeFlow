package com.example.flowflow.freeflow;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arnorymir on 12/09/14.
 */
public class CellPath {

    private ArrayList<Coordinate> mPath = new ArrayList<Coordinate>();
    private int mColorID;

    public CellPath(int colorID) {
        mColorID = colorID;
    }

    public void append(Coordinate coordinate) {
        mPath.add(coordinate);
    }

    public List<Coordinate> getCoordinates() {
        return mPath;
    }

    public void reset() {
        mPath.clear();
    }

    public boolean isEmpty() {
        return mPath.isEmpty();
    }

    public int getColorID() {
        return mColorID;
    }

    public void popToCoordinate(Coordinate coordinate) {
        int index = mPath.indexOf(coordinate);
        if(index >= 0) {
            for (int i = mPath.size()-1; i > index; --i) {
                mPath.remove(i);
            }
        }
    }

    public void popPastCoordinate(Coordinate coordinate) {
        int index = mPath.indexOf(coordinate);
        if(index > 0) {
            popToCoordinate(mPath.get(index - 1));
        }
    }

    public Coordinate getFirstCoordinate() {
        if(!isEmpty()) {
            return mPath.get(0);
        }
        return null;
    }

    public Coordinate getLastCoordinate() {
        return mPath.get(mPath.size() - 1);
    }

    public int length() {
        return mPath.size();
    }

    public boolean contains(Coordinate coordinate) {
        return getCoordinates().contains(coordinate);
    }
}
