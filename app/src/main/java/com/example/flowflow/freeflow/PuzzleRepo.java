package com.example.flowflow.freeflow;
import android.util.Log;

import java.util.List;


/**
 * Created by arnorymir on 20/09/14.
 */
// singleton class for all puzzles
public class PuzzleRepo {

    // list of all puzzles. both regular and mania.
    public List<Puzzle> mPuzzles;
    public int sizeOf5x5 = 0;
    public int sizeOf6x6 = 0;
    public int sizeOf7x7 = 0;

    private static PuzzleRepo mInstance = new PuzzleRepo();
    public static PuzzleRepo getInstance() {
        return mInstance;
    }

    private PuzzleRepo(){}

    public Puzzle getPreviousPuzzle(Puzzle puzzle) {
        int size = puzzle.getSize();
        int maxIndex;
        int minIndex;
        switch(size) {
            case 5:
                minIndex = 0;
                maxIndex = sizeOf5x5 - 1;
                break;
            case 6:
                minIndex = sizeOf5x5;
                maxIndex = sizeOf5x5 + sizeOf6x6 - 1;
                break;
            case 7:
                minIndex = sizeOf5x5 + sizeOf6x6;
                maxIndex = sizeOf5x5 + sizeOf6x6 + sizeOf7x7 - 1;
                break;
            default:
                return null;
        }
        int id = puzzle.getID();
        if(id <= minIndex) {
            return null;
        }
        return mPuzzles.get(id - 1);
    }

    public Puzzle getNextPuzzle(Puzzle puzzle) {
        int size = puzzle.getSize();
        int maxIndex;
        int minIndex;
        switch(size) {
            case 5:
                minIndex = 0;
                maxIndex = sizeOf5x5 - 1;
                break;
            case 6:
                minIndex = sizeOf5x5;
                maxIndex = sizeOf5x5 + sizeOf6x6 - 1;
                break;
            case 7:
                minIndex = sizeOf5x5 + sizeOf6x6;
                maxIndex = sizeOf5x5 + sizeOf6x6 + sizeOf7x7 - 1;
                break;
            default:
                return null;
        }
        int id = puzzle.getID();
        if(id >= maxIndex) {
            return null;
        }
        return mPuzzles.get(id + 1);
    }

    public Puzzle getPuzzleByID(int id) {
        return mPuzzles.get(id);
    }

    public boolean isFirstOfItsSize(Puzzle puzzle) {
        int size = puzzle.getSize();
        int minIndex;
        int maxIndex;
        switch(size) {
            case 5:
                minIndex = 0;
                maxIndex = sizeOf5x5 - 1;
                break;
            case 6:
                minIndex = sizeOf5x5;
                maxIndex = sizeOf5x5 + sizeOf6x6 - 1;
                break;
            case 7:
                minIndex = sizeOf5x5 + sizeOf6x6;
                maxIndex = sizeOf5x5 + sizeOf6x6 + sizeOf7x7 - 1;
                break;
            default:
                minIndex = -1;
                maxIndex = -1;
        }
        return puzzle.getID() == minIndex;
    }

    public boolean isLastOfItsSize(Puzzle puzzle) {
        int size = puzzle.getSize();
        int minIndex;
        int maxIndex;
        switch(size) {
            case 5:
                minIndex = 0;
                maxIndex = sizeOf5x5 - 1;
                break;
            case 6:
                minIndex = sizeOf5x5;
                maxIndex = sizeOf5x5 + sizeOf6x6 - 1;
                break;
            case 7:
                minIndex = sizeOf5x5 + sizeOf6x6;
                maxIndex = sizeOf5x5 + sizeOf6x6 + sizeOf7x7 - 1;
                break;
            default:
                minIndex = -1;
                maxIndex = -1;
        }
        return puzzle.getID() == maxIndex;
    }

    public int getPuzzleNumber(Puzzle puzzle) {
        
    }
}
