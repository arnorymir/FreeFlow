package com.example.flowflow.freeflow;

/**
 * Created by bjornorri on 15/09/14.
 */
public class Game {

    private Puzzle mPuzzle;
    private int numOccupiedCells;
    private int numMoves;

    public Game(Puzzle puzzle) {
        mPuzzle = puzzle;
        numOccupiedCells = 0;
        numMoves = 0;
    }

    public boolean isWon() {
        int size = mPuzzle.getSize();
        return numOccupiedCells == size * size;
    }

    public void setOccupiedCells(int num) {
        numOccupiedCells = num;
    }

    public Puzzle getPuzzle() {
        return mPuzzle;
    }

    public int getMoves() {
        return numMoves;
    }

    public void addMove() {
        numMoves++;
    }
}
