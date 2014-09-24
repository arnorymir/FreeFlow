package com.example.flowflow.freeflow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "PUZZLES_DB";
    public static final int DB_VERSION = 2;

    public static final String TablePuzzle = "puzzles";
    public static final String[] TablePuzzleCols = { "puzzleId", "moves", "finished"};

    private static final String sqlCreateTablePuzzles =
            "CREATE TABLE puzzles(" +
                    " puzzleId INTEGER PRIMARY KEY NOT NULL," +
                    " moves INTEGER," +
                    " finished INTEGER " +
                    ");";

    private static final String sqlDropTablePuzzles =
            "DROP TABLE IF EXISTS puzzles;";

    public DbHelper( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( sqlCreateTablePuzzles );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( sqlDropTablePuzzles );
        onCreate( db );
    }
}