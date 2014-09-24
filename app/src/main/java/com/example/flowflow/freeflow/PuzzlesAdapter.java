package com.example.flowflow.freeflow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PuzzlesAdapter {

    SQLiteDatabase db;
    DbHelper dbHelper;
    Context context;

    public PuzzlesAdapter( Context c ) {
        context = c;
    }

    public PuzzlesAdapter openToRead() {
        dbHelper = new DbHelper( context );
        db = dbHelper.getReadableDatabase();
        return this;
    }

    public PuzzlesAdapter openToWrite() {
        dbHelper = new DbHelper( context );
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public long insertPuzzle( int id, int moves , boolean finished ) {
        String[] cols = DbHelper.TablePuzzleCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put(cols[0], id);
        contentValues.put(cols[1], moves);
        contentValues.put(cols[2], finished ? "1":"0");
        openToWrite();
        long value = db.insert(DbHelper.TablePuzzle, null, contentValues);
        close();
        return value;
    }

    public long updatePuzzle(int id, int moves, boolean finished ) {
        String[] cols = DbHelper.TablePuzzleCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], moves);
        contentValues.put( cols[2], finished ? "1" : "0" );
        openToWrite();
        long value = db.update(DbHelper.TablePuzzle,
                contentValues,
                cols[1] + "=" + id, null );
        close();
        return value;
    }

    // sets all puzzles to unfinished but keeps the best move for
    // each puzzle
    public long resetAllPuzzlesFinished() {
        String[] cols = DbHelper.TablePuzzleCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[2], "0");
        openToWrite();
        long value = db.update(DbHelper.TablePuzzle,
                contentValues,
                null, null );
        close();
        return value;
    }

    //set all puzzles to unfinished and removes all best moves.
    public long resetAllPuzzlesFinishedAndMove() {
        String[] cols = DbHelper.TablePuzzleCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], "null");
        contentValues.put( cols[2], "0");
        openToWrite();
        long value = db.update(DbHelper.TablePuzzle,
                contentValues,
                null, null );
        close();
        return value;
    }

    public Cursor queryPuzzles() {
        openToRead();
        Cursor cursor = db.query( DbHelper.TablePuzzle,
                DbHelper.TablePuzzleCols, null, null, null, null, null);
        return cursor;
    }

    public Cursor queryPuzzle(int id) {
        openToRead();
        String[] cols = DbHelper.TablePuzzleCols;
        Cursor cursor = db.query( DbHelper.TablePuzzle,
                cols, cols[0] + "=" + id, null, null, null, null);
        return cursor;
    }

}