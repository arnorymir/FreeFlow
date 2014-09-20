package com.example.flowflow.freeflow;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class PlayActivity extends ActionBarActivity {

    private Board mBoard;
    private Game mGame;
    private boolean mGameWon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mBoard = (Board) findViewById(R.id.playBoard);
        int size = getIntent().getExtras().getInt("BoardSize");

        // Create example puzzle instance
        Dot[] dots = new Dot[]{new Dot(0, 0, 0), new Dot(0, size - 1, 0), new Dot(1, 0, 1), new Dot(1, size - 1, 1), new Dot(2, 0, 2), new Dot(size-1, size - 1, 2)};
        Puzzle puzzle = new Puzzle(size, dots);
        mBoard.setPuzzle(puzzle);
        mGame = new Game(puzzle);
        mGameWon = false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.play, menu);
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

    public void update() {
        if(!mGameWon) {
            Integer numOccupiedCells = mBoard.numberOfOccupiedCells();
            if(numOccupiedCells != null) {
                mGame.setOccupiedCells(numOccupiedCells.intValue());
            }
            if(mGame.isWon()) {
                Toast.makeText(getApplicationContext(), "You won!", Toast.LENGTH_SHORT).show();
                mGameWon = true;
            }
        }
    }
}
