package com.example.flowflow.freeflow;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class TimeTrialActivity extends ActionBarActivity {

    private Board mBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_trial);

        String difficulty = getIntent().getExtras().getString("difficulty");
        Puzzle puzzle = PuzzleRepo.getInstance().getPuzzleByID(0);

        // Board
        mBoard = (Board) findViewById(R.id.timeTrialBoard);
        mBoard.setPuzzle(puzzle);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set the selected color scheme.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mBoard.setColorScheme(prefs.getString("prefColorScheme", "Rainbow"));
        mBoard.invalidate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.time_trial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
