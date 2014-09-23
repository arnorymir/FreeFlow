package com.example.flowflow.freeflow;
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

    public Puzzle getPuzzleByID(int id) {
        if(id < 0 || id >= mPuzzles.size()) {
            return null;
        }
        return mPuzzles.get(id);
    }
}
