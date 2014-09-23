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
    private TextView mTitleLabel;
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

        // Title label
        mTitleLabel = (TextView)findViewById(R.id.playTitleLabel);
        mTitleLabel.setText("Puzzle " + (mGame.getPuzzle().getID() + 1));

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
                goToPreviousPuzzle();
            }
        });

        // Next button
        mNextButton = (Button)findViewById(R.id.playNextButton);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPuzzle();
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


        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Nice!")
                .setNegativeButton(R.string.solveAgain, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Set this puzzle up again.
                        reset();
                    }
                });

        final Puzzle nextPuzzle = mPuzzleRepo.getNextPuzzle(mGame.getPuzzle());
        if(nextPuzzle != null) {
            dialog
                .setMessage("You solved this puzzle in " + mGame.getMoves() + " moves!")
                .setPositiveButton(R.string.nextPuzzle, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    goToNextPuzzle();
                }
            });
        }
        else {
            dialog
                .setMessage("You solved this puzzle in " + mGame.getMoves() + " moves!\nThis was the last puzzle.")
                .setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
        dialog.show();
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
        mTitleLabel.setText("Puzzle " + (mGame.getPuzzle().getID() + 1));
    }

    private void goToPreviousPuzzle() {
        Puzzle prevPuzzle = mPuzzleRepo.getPreviousPuzzle(mGame.getPuzzle());
        if(prevPuzzle != null) {
            mGame = new Game(prevPuzzle);
            mBoard.setPuzzle(prevPuzzle);
            reset();
        }
    }

    private void goToNextPuzzle() {
        Puzzle nextPuzzle = mPuzzleRepo.getNextPuzzle(mGame.getPuzzle());
        if(nextPuzzle != null) {
            mGame = new Game(nextPuzzle);
            mBoard.setPuzzle(nextPuzzle);
            reset();
        }
    }
}
