package com.example.flowflow.freeflow;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easyandroidanimations.library.ExplodeAnimation;


public class PlayActivity extends ActionBarActivity {

    private Board mBoard;
    private TextView mMoveLabel;
    private Game mGame;
    private boolean mGameWon;
    private PuzzleRepo puzzleRepo = PuzzleRepo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mBoard = (Board) findViewById(R.id.playBoard);

        int id = getIntent().getExtras().getInt("Id");
        Puzzle puzzle = puzzleRepo.mPuzzles.get(id);

        mBoard.setPuzzle(puzzle);
        mGame = new Game(puzzle);
        mGameWon = false;

        mMoveLabel = (TextView) findViewById(R.id.playMoveLabel);
        mMoveLabel.setText("Moves: " + mGame.getMoves());
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
        mMoveLabel.setText("Moves: " + mGame.getMoves());
        if(!mGameWon) {
            Integer numOccupiedCells = mBoard.numberOfOccupiedCells();
            if(numOccupiedCells != null) {
                mGame.setOccupiedCells(numOccupiedCells.intValue());
            }
            if(mGame.isWon()) {
                mGameWon = true;
                new ExplodeAnimation(mBoard).animate();
            }
        }
    }

    public void addMove() {
        mGame.addMove();
    }
}
