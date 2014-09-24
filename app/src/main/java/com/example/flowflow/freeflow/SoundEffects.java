package com.example.flowflow.freeflow;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Johann on 24/09/2014.
 */
public class SoundEffects {

    public SoundPool sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    public static int soundIds[] = new int[5];

    private static SoundEffects mInstance = new SoundEffects();

    public static SoundEffects getInstance() {return mInstance;}

    public void loadSounds(Context c){
        SharedPreferences SharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        Boolean sound = SharedPrefs.getBoolean("prefSound", true);

        if(sound) {
            soundIds[0] = sp.load(c, R.raw.connect, 1);
            soundIds[1] = sp.load(c, R.raw.next, 1);
            soundIds[2] = sp.load(c, R.raw.click, 1);
            soundIds[3] = sp.load(c, R.raw.battle010, 1);
        }
        else{
            sp.unload(soundIds[0]);
            sp.unload(soundIds[1]);
            sp.unload(soundIds[2]);
            sp.unload(soundIds[3]);
        }
    }

}
