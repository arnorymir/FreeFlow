package com.example.flowflow.freeflow;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class TimeTrialPickerActivity extends ActionBarActivity {

    private Button mEasyButton;
    private Button mMediumButton;
    private Button mHardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_trial_picker);

        mEasyButton = (Button) findViewById(R.id.easyButton);
        mMediumButton = (Button) findViewById(R.id.mediumButton);
        mHardButton = (Button) findViewById(R.id.hardButton);

        mEasyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimeTrialActivity.class);
                intent.putExtra("difficulty", "easy");
                startActivity(intent);
            }
        });

        mMediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimeTrialActivity.class);
                intent.putExtra("difficulty", "medium");
                startActivity(intent);
            }
        });

        mHardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimeTrialActivity.class);
                intent.putExtra("difficulty", "hard");
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.time_trial_picker, menu);
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
