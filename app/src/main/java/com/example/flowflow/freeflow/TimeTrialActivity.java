package com.example.flowflow.freeflow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ExplodeAnimation;


public class TimeTrialActivity extends ActionBarActivity {

    private Board mBoard;
    private PuzzleRepo mPuzzleRepo;
    private Puzzle mPuzzle;
    private int mSolvedPuzzles;
    private CountDownTimer mTimer;
    private TextView mSolvedLabel;
    private TextView mTimeLabel;
    private int TIME; // In seconds.
    private int mScorePerPuzzle;
    private SoundEffects soundEffects = SoundEffects.getInstance();
    private Boolean shouldVibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_trial);

        soundEffects.loadSounds(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        shouldVibrate = prefs.getBoolean("prefVibration",true);

        TIME = 30;
        mPuzzleRepo = PuzzleRepo.getInstance();
        mSolvedPuzzles = 0;

        String difficulty = getIntent().getExtras().getString("difficulty");
        Puzzle puzzle = null;
        if(difficulty.equals("easy")) {
            mScorePerPuzzle = 10;
            puzzle = mPuzzleRepo.getFirstPuzzleOfSize(5);
        }
        else if(difficulty.equals("medium")) {
            mScorePerPuzzle = 20;
            puzzle = mPuzzleRepo.getFirstPuzzleOfSize(6);
        }
        else if(difficulty.equals("hard")) {
            mScorePerPuzzle = 30;
            puzzle = mPuzzleRepo.getFirstPuzzleOfSize(7);
        }

        // Puzzle
        mPuzzle = puzzle;

        // Board
        mBoard = (Board) findViewById(R.id.timeTrialBoard);
        mBoard.setPuzzle(mPuzzle);

        // Puzzles solved label
        mSolvedLabel = (TextView)findViewById(R.id.solvedLabel);
        mSolvedLabel.setText("Puzzles solved: " + mSolvedPuzzles);

        // Time label
        mTimeLabel = (TextView)findViewById(R.id.timeLabel);
        mTimeLabel.setText("Time: " + TIME);

        // Timer
        mTimer = new CountDownTimer(TIME * 1000, 1 * 1000) {

            public void onTick(long millisUntilFinished) {
                mTimeLabel.setText("Time: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTimeLabel.setText("Time: " + 0);
                displayTimeoutDialog();
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set the selected color scheme.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mBoard.setColorScheme(prefs.getString("prefColorScheme", "Rainbow"));
        mBoard.invalidate();
        shouldVibrate = prefs.getBoolean("prefVibration",true);
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

    private void displayTimeoutDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Time's up!")
                .setMessage("You solved " + mSolvedPuzzles + " puzzles in " + TIME + " seconds.\nYour score is " + mScorePerPuzzle * mSolvedPuzzles + "!")
                .setPositiveButton("Play again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reset();
                    }
                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        dialog.show();
    }

    private void displayAllSolvedDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("That's it!")
                .setMessage("You solved all the puzzles!\nYour score is " + mSolvedPuzzles * mScorePerPuzzle + "!")
                .setPositiveButton("Play again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reset();
                    }
                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        dialog.show();
    }

    private void reset() {
        mSolvedPuzzles = 0;
        mSolvedLabel.setText("Puzzles solved: " + mSolvedPuzzles);
        mTimeLabel.setText("Time: " + TIME);
        mPuzzle = mPuzzleRepo.getFirstPuzzleOfSize(mPuzzle.getSize());
        mBoard.setPuzzle(mPuzzle);
        mBoard.invalidate();
        mTimer = new CountDownTimer(TIME * 1000, 1 * 1000) {

            public void onTick(long millisUntilFinished) {
                mTimeLabel.setText("Time: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTimeLabel.setText("Time: " + 0);
                displayTimeoutDialog();
            }
        }.start();
    }

    public void solvedPuzzle() {
        mSolvedPuzzles++;
        mSolvedLabel.setText("Puzzles solved: " + mSolvedPuzzles);
        if(mPuzzle != null) {
            Puzzle nextPuzzle = mPuzzleRepo.getNextPuzzle(mPuzzle);
            if(nextPuzzle != null) {
                mPuzzle = nextPuzzle;
            }
            else {
                displayAllSolvedDialog();
                mTimer.cancel();
                return;
            }

        }
        showLabels(false);
        new ExplodeAnimation(mBoard)
                .setListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mBoard.setPuzzle(mPuzzle);
                        mBoard.setVisibility(View.VISIBLE);
                        showLabels(true);
                    }
                })
                .animate();
    }

    // Show and hide labels
    private void showLabels(boolean show) {
        int visibility = View.VISIBLE;
        if(!show) {
            visibility = View.GONE;
        }
        mSolvedLabel.setVisibility(visibility);
        mTimeLabel.setVisibility(visibility);
    }
    public Boolean getShouldVibrate(){
        return shouldVibrate;
    }
}
