package com.example.flowflow.freeflow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
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
    private TextView mSolvedLabel;
    private Button mResetButton;
    private Button mPrevButton;
    private Button mNextButton;
    private Game mGame;
    private PuzzleRepo mPuzzleRepo = PuzzleRepo.getInstance();
    private SoundEffects soundEffects = SoundEffects.getInstance();
    private Boolean shouldVibrate;
    private PuzzlesAdapter mPuzzlesAdapter = new PuzzlesAdapter(this);
    private DbHelper dbHelper = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        soundEffects.loadSounds(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        shouldVibrate = prefs.getBoolean("prefVibration",true);
        int id = getIntent().getExtras().getInt("Id");
        Puzzle puzzle = mPuzzleRepo.getPuzzleByID(id);

        // Board
        mBoard = (Board) findViewById(R.id.playBoard);
        mBoard.setPuzzle(puzzle);
        mGame = new Game(puzzle);

        // Move label
        mMoveLabel = (TextView) findViewById(R.id.playMoveLabel);
        updateMoves();

        // Best and solved labels
        mBestLabel = (TextView) findViewById(R.id.playBestLabel);
        mSolvedLabel = (TextView) findViewById(R.id.playSolvedLabel);
        updateBestAndSolved();


        // Pipe label
        mPipeLabel = (TextView) findViewById(R.id.playPipeLabel);
        updatePipe();

        // Title label
        mTitleLabel = (TextView)findViewById(R.id.playTitleLabel);
        mTitleLabel.setText("Puzzle " + mPuzzleRepo.getPuzzleNumber(mGame.getPuzzle()));

        // Reset button
        mResetButton = (Button) findViewById(R.id.playResetButton);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundEffects.sp.play(SoundEffects.soundIds[2], 1, 1, 1, 0, (float)0.5);
                reset();
            }
        });

        // Previous button
        mPrevButton = (Button)findViewById(R.id.playPreviousButton);
        mPrevButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundEffects.sp.play(SoundEffects.soundIds[1], 1, 1, 1, 0, (float)1.5);
                goToPreviousPuzzle();
            }
        });

        // Next button
        mNextButton = (Button)findViewById(R.id.playNextButton);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundEffects.sp.play(SoundEffects.soundIds[1], 1, 1, 1, 0, (float)1.5);
                goToNextPuzzle();
            }
        });
        toggleButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBestAndSolved();

        // Set the selected color scheme.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mBoard.setColorScheme(prefs.getString("prefColorScheme", "Rainbow"));
        shouldVibrate = prefs.getBoolean("prefVibration",true);
        mBoard.invalidate();
        soundEffects.loadSounds(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_OK);
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
            mPuzzlesAdapter.updatePuzzle(mGame.getPuzzle().getID(),mGame.getMoves(),true);
            displayDialog();
        }
    }

    public void updateMoves() {
        mMoveLabel.setText("Moves: " + mGame.getMoves());
    }

    public void updatePipe() {
        mPipeLabel.setText("Pipe: " + mBoard.numberOfOccupiedCells() + " / " + mGame.getPuzzle().getSize() * mGame.getPuzzle().getSize());
    }

    public void updateBestAndSolved() {
        int bestMove = dbHelper.getBestMove(mGame.getPuzzle().getID());
        if(bestMove == 0) {
            mBestLabel.setText("Best: -");
            mSolvedLabel.setVisibility(View.GONE);
        }
        else {
            mBestLabel.setText("Best: " + bestMove);
            mSolvedLabel.setVisibility(View.VISIBLE);
        }
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
                        soundEffects.sp.play(SoundEffects.soundIds[2], 1, 1, 1, 0, 1);
                        goToNextPuzzle();
                    }
                });
        }
        else {
            soundEffects.sp.play(SoundEffects.soundIds[3], 1, 1, 1, 0, 1);
            dialog
                .setMessage("You solved this puzzle in " + mGame.getMoves() + " moves!\nThis was the last puzzle.")
                .setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        soundEffects.sp.play(SoundEffects.soundIds[2], 1, 1, 1, 0, 1);
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
        updateBestAndSolved();
        updatePipe();
        toggleButtons();
        mTitleLabel.setText("Puzzle " + mPuzzleRepo.getPuzzleNumber(mGame.getPuzzle()));
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
    public Boolean getShouldVibrate(){
        return shouldVibrate;
    }
}
