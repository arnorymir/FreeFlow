package com.example.flowflow.freeflow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arnorymir on 12/09/14.
 */
public class CellPath {

    private ArrayList<Coordinate> mPath = new ArrayList<Coordinate>();

    public void append(Coordinate coordinate) {
        int index = mPath.indexOf(coordinate);
        if (index >= 0) {
            for (int i=mPath.size()-1; i > index; --i) {
                mPath.remove(i);
            }
        }
        else {
            mPath.add(coordinate);
        }
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
}
