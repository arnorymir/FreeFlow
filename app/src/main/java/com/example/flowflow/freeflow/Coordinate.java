package com.example.flowflow.freeflow;

/**
 * Created by bjornorri on 12/09/14.
 */
public class Coordinate {

    private int mRow;
    private int mCol;

    // Constructor
    Coordinate(int row, int col) {
        mRow = row;
        mCol = col;
    }

    // Getters for member variables
    public int getRow() {
        return mRow;
    }

    public int getCol() {
        return mCol;
    }

    // Implement equality
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Coordinate)) {
            return false;
        }
        Coordinate otherCoordinate = (Coordinate) other;
        return otherCoordinate.getCol() == this.getCol()&& otherCoordinate.getRow() == this.getRow();
    }
}
