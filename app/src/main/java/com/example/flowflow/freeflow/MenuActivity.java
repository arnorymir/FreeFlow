package com.example.flowflow.freeflow;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MenuActivity extends ActionBarActivity {

    Button mPlayButton;
    Button mTimeTrialButton;
    Button mHighScoresButton;
    Button mSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Extract the buttons from the view
        mPlayButton = (Button) findViewById(R.id.playButton);
        mTimeTrialButton = (Button) findViewById(R.id.timeTrialButton);
        mHighScoresButton = (Button) findViewById(R.id.highScoresButton);
        mSettingsButton = (Button) findViewById(R.id.settingsButton);

        // Assign onClick listeners to the buttons
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MenuActivity", "Pressed Play button");
                Intent intent = new Intent(getApplicationContext(), GridPickerActivity.class);
                startActivity(intent);
            }
        });
        mTimeTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MenuActivity", "Pressed Time Trial button");
                Intent intent = new Intent(getApplicationContext(), TimeTrialPickerActivity.class);
                startActivity(intent);
            }
        });
        mHighScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MenuActivity", "Pressed High Scores button");
                Intent intent = new Intent(getApplicationContext(), HighScoresActivity.class);
                startActivity(intent);
            }
        });
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MenuActivity", "Pressed Settings button");
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
