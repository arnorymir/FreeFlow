package com.example.flowflow.freeflow;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.easyandroidanimations.library.ExplodeAnimation;


public class PlayActivity extends ActionBarActivity {

    private Board mBoard;
    private Game mGame;
    private boolean mGameWon;
    private PuzzleRepo puzzleRepo = PuzzleRepo.getInstance();
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mBoard = (Board) findViewById(R.id.playBoard);
        mId = getIntent().getExtras().getInt("Id");

        Puzzle puzzle = puzzleRepo.mPuzzles.get(mId);

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
        // Need to invalidate the whole view to allow the finger circle to be drawn outside the board.
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        if(!mGameWon) {
            Integer numOccupiedCells = mBoard.numberOfOccupiedCells();
            if(numOccupiedCells != null) {
                mGame.setOccupiedCells(numOccupiedCells.intValue());
            }
            if(mGame.isWon()) {
                mGameWon = true;
                new ExplodeAnimation(mBoard).animate();
                //Toast.makeText(getApplicationContext(), "You won!", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
