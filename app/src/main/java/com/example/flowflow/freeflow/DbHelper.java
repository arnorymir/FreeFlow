package com.example.flowflow.freeflow;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    public boolean isFinished(int id) {

        String query = "SELECT  * FROM puzzles WHERE puzzleId =" + id ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int returnValue = 0;
        if (cursor.moveToFirst()) {
            do {
                returnValue = Integer.parseInt(cursor.getString(2));
            } while (cursor.moveToNext());
        }

       if(returnValue == 0)
       {
           return false;
       }
        return true;

    }


}