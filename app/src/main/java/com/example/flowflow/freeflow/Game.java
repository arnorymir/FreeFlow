package com.example.flowflow.freeflow;

/**
 * Created by bjornorri on 15/09/14.
 */
public class Game {

    private Puzzle mPuzzle;
    private int numOccupiedCells;

    public Game(Puzzle puzzle) {
        mPuzzle = puzzle;
        numOccupiedCells = 0;
    }

    public boolean isWon() {
        int size = mPuzzle.getSize();
        return numOccupiedCells == size * size;
    }

    public void setOccupiedCells(int num) {
        numOccupiedCells = num;
    }
}