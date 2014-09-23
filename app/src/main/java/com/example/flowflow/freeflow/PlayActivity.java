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
    private TextView mBestLabel;
    private TextView mPipeLabel;
    private Game mGame;
    private boolean mGameWon;
    private PuzzleRepo puzzleRepo = PuzzleRepo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        int id = getIntent().getExtras().getInt("Id");
        Puzzle puzzle = puzzleRepo.mPuzzles.get(id);

        // Board
        mBoard = (Board) findViewById(R.id.playBoard);
        mBoard.setPuzzle(puzzle);
        mGame = new Game(puzzle);
        mGameWon = false;

        // Move label
        mMoveLabel = (TextView) findViewById(R.id.playMoveLabel);
        updateMoves();

        // Best label
        mBestLabel = (TextView) findViewById(R.id.playBestLabel);
        mBestLabel.setText("Best: -");

        // Pipe label
        mPipeLabel = (TextView) findViewById(R.id.playPipeLabel);
        updatePipe();
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
        updateMoves();
        updatePipe();
        if(!mGameWon) {
            int numOccupiedCells = mBoard.numberOfOccupiedCells();
            mGame.setOccupiedCells(numOccupiedCells);
            if(mGame.isWon()) {
                mGameWon = true;
                Log.i("", "Won");
            }
        }
    }

    public void updateMoves() {
        mMoveLabel.setText("Moves: " + mGame.getMoves());
    }

    public void updatePipe() {
        mPipeLabel.setText("Pipe: " + mBoard.numberOfOccupiedCells() + " / " + mGame.getPuzzle().getSize() * mGame.getPuzzle().getSize());
    }

    public void addMove() {
        mGame.addMove();
    }
}
