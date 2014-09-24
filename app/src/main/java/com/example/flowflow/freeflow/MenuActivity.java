package com.example.flowflow.freeflow;

import android.content.Intent;
import org.w3c.dom.Element;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends ActionBarActivity {

    Button mPlayButton;
    Button mTimeTrialButton;
    Button mHighScoresButton;
    Button mSettingsButton;
    private PuzzleRepo mPuzzleRepo = PuzzleRepo.getInstance();

    private SoundEffects soundEffects = SoundEffects.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        soundEffects.loadSounds(this);

        try {
            List<Puzzle> puzzles = new ArrayList<Puzzle>();
            readPuzzle(getAssets().open("packs/regular.xml"), puzzles);
            readPuzzle(getAssets().open("packs/mania.xml"), puzzles);
            mPuzzleRepo.mPuzzles = puzzles;

        }
        catch ( Exception e ) {
            e.printStackTrace();
        }

        // Extract the buttons from the view
        mPlayButton = (Button) findViewById(R.id.playButton);
        mTimeTrialButton = (Button) findViewById(R.id.timeTrialButton);
        mHighScoresButton = (Button) findViewById(R.id.highScoresButton);
        mSettingsButton = (Button) findViewById(R.id.settingsButton);

        // Assign onClick listeners to the buttons
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                soundEffects.sp.play(SoundEffects.soundIds[2], 1, 1, 1, 0, 1);

                Log.d("MenuActivity", "Pressed Play button");
                Intent intent = new Intent(getApplicationContext(), GridPickerActivity.class);
                startActivity(intent);
            }
        });
        mTimeTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundEffects.sp.play(SoundEffects.soundIds[2], 1, 1, 1, 0, 1);
                Log.d("MenuActivity", "Pressed Time Trial button");
                Intent intent = new Intent(getApplicationContext(), TimeTrialPickerActivity.class);
                startActivity(intent);
            }
        });
        mHighScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundEffects.sp.play(SoundEffects.soundIds[2], 1, 1, 1, 0, 1);
                Log.d("MenuActivity", "Pressed High Scores button");
                Intent intent = new Intent(getApplicationContext(), HighScoresActivity.class);
                startActivity(intent);
            }
        });
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundEffects.sp.play(SoundEffects.soundIds[2], 1, 1, 1, 0, 1);
                Log.d("MenuActivity", "Pressed Settings button");
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });


    }
    @Override
    public void onResume() {
        super.onResume();
        soundEffects.loadSounds(this);
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

    private void readPuzzle(InputStream inputStream, List<Puzzle> puzzles) {
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            int sizeOf5x5 = mPuzzleRepo.sizeOf5x5;
            int sizeOf6x6 = mPuzzleRepo.sizeOf6x6;
            int sizeOf7x7 = mPuzzleRepo.sizeOf7x7;

            NodeList nList = doc.getElementsByTagName( "puzzle" );
            for (int c = 0; c < nList.getLength(); c++) {
                Node nNode = nList.item(c);
                if ( nNode.getNodeType() == Node.ELEMENT_NODE ) {
                    Element eNode = (Element) nNode;


                    String size = eNode.getElementsByTagName("size").item(0).getFirstChild().getNodeValue();
                    String stringDots = eNode.getElementsByTagName("flows").item(0).getFirstChild().getNodeValue();

                    //remove all , () and space symbol in the string.
                    String dotsNumbers = stringDots.replaceAll("[, ; ()]","");

                    List<Dot> dots = new ArrayList<Dot>();

                    int colorID = 0;
                    for(int i = 0; i < dotsNumbers.length(); i += 4)
                    {
                            int x = Character.getNumericValue(dotsNumbers.charAt(i));
                            int y = Character.getNumericValue(dotsNumbers.charAt(i + 1));
                            int x1 = Character.getNumericValue(dotsNumbers.charAt(i + 2));
                            int y1 = Character.getNumericValue(dotsNumbers.charAt(i + 3));

                            Dot dot = new Dot(x, y, colorID);
                            Dot dot1 = new Dot(x1, y1, colorID);
                            dots.add(dot);
                            dots.add(dot1);
                            colorID++;

                    }
                    //converting arraylist to array of dots.
                    Dot[] returnDot = new Dot[dots.size()];
                    returnDot = dots.toArray(returnDot);

                    switch (Integer.parseInt(size)) {
                        case 5:
                            sizeOf5x5 ++;
                            break;
                        case 6:
                            sizeOf6x6++;
                            break;
                        case 7:
                            sizeOf7x7++;
                            // This is a dirty workaround for an unexplainable bug.
                            c = 14;
                            break;
                    }
              
                    //adding new puzzle. c is the id of the puzzle.
                    puzzles.add(new Puzzle(c, Integer.parseInt(size), returnDot));
                }
            }

            mPuzzleRepo.sizeOf5x5 = sizeOf5x5;
            mPuzzleRepo.sizeOf6x6 = sizeOf6x6;
            mPuzzleRepo.sizeOf7x7 = sizeOf7x7;
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}

