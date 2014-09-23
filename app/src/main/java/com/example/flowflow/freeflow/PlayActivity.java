package com.example.flowflow.freeflow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easyandroidanimations.library.ExplodeAnimation;


public class PlayActivity extends ActionBarActivity {

    private Board mBoard;
    private TextView mMoveLabel;
    private TextView mBestLabel;
    private TextView mPipeLabel;
    private Button mResetButton;
    private Game mGame;
    private PuzzleRepo mPuzzleRepo = PuzzleRepo.getInstance();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        id = getIntent().getExtras().getInt("Id");
        Puzzle puzzle = mPuzzleRepo.getPuzzleByID(id);

        // Board
        mBoard = (Board) findViewById(R.id.playBoard);
        mBoard.setPuzzle(puzzle);
        mGame = new Game(puzzle);

        // Move label
        mMoveLabel = (TextView) findViewById(R.id.playMoveLabel);
        updateMoves();

        // Best label
        mBestLabel = (TextView) findViewById(R.id.playBestLabel);
        mBestLabel.setText("Best: -");

        // Pipe label
        mPipeLabel = (TextView) findViewById(R.id.playPipeLabel);
        updatePipe();

        // Reset button
        mResetButton = (Button) findViewById(R.id.playResetButton);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
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
        int numOccupiedCells = mBoard.numberOfOccupiedCells();
        mGame.setOccupiedCells(numOccupiedCells);
        if(mGame.isWon()) {
            mBoard.setAllowTouch(false);
            displayDialog();
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

    private void displayDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Nice!")
                .setMessage("You solved this puzzle in " + mGame.getMoves() + " moves!")
                .setPositiveButton(R.string.nextPuzzle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Set up the next puzzle.
                        id++;
                        Puzzle nextPuzzle = mPuzzleRepo.getPuzzleByID(id);
                        if(nextPuzzle != null) {
                            mBoard.setPuzzle(nextPuzzle);
                            reset();
                        }
                        else {
                            Log.i("", "No more puzzles :(");
                        }
                    }
                })
                .setNegativeButton(R.string.solveAgain, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Set this puzzle up again.
                        reset();
                    }
                })
                .show();
    }

    private void reset() {
        mGame.reset();
        mBoard.reset();
        mBoard.invalidate();
        updateMoves();
        updatePipe();
    }
}
