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
    private Button mPrevButton;
    private Button mNextButton;
    private Game mGame;
    private PuzzleRepo mPuzzleRepo = PuzzleRepo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        int id = getIntent().getExtras().getInt("Id");
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

        // Previous button
        mPrevButton = (Button)findViewById(R.id.playPreviousButton);
        mPrevButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Puzzle prevPuzzle = mPuzzleRepo.getPreviousPuzzle(mGame.getPuzzle());
                mBoard.setPuzzle(prevPuzzle);
                mGame = new Game(prevPuzzle);
                reset();
            }
        });

        // Next button
        mNextButton = (Button)findViewById(R.id.playNextButton);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Puzzle nextPuzzle = mPuzzleRepo.getNextPuzzle(mGame.getPuzzle());
                mBoard.setPuzzle(nextPuzzle);
                mGame = new Game(nextPuzzle);
                reset();
            }
        });
        toggleButtons();
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
                        Puzzle nextPuzzle = mPuzzleRepo.getNextPuzzle(mGame.getPuzzle());
                        if(nextPuzzle != null) {
                            mBoard.setPuzzle(nextPuzzle);
                            mGame = new Game(nextPuzzle);
                            reset();
                        }
                        else {

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

    // Toggle if buttons should be enabled or disabled
    private void toggleButtons() {
        if(mPuzzleRepo.isFirstOfItsSize(mGame.getPuzzle())) {
            mPrevButton.setVisibility(View.INVISIBLE);
        }
        else {
            mPrevButton.setVisibility(View.VISIBLE);
        }
        if(mPuzzleRepo.isLastOfItsSize(mGame.getPuzzle())) {
            mNextButton.setVisibility(View.INVISIBLE);
        }
        else {
            mNextButton.setVisibility(View.VISIBLE);
        }
    }

    private void reset() {
        mGame.reset();
        mBoard.reset();
        mBoard.invalidate();
        updateMoves();
        updatePipe();
        toggleButtons();
    }
}
