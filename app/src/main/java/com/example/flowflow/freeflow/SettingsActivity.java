package com.example.flowflow.freeflow;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;


import java.util.List;

public class SettingsActivity extends PreferenceActivity {
    private PuzzlesAdapter puzzlesAdapter = new PuzzlesAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        Preference button = (Preference)findPreference("clearButton");
        final Context context = this;
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                        .setTitle("Really?")
                        .setMessage("Are you sure you want to mark all the puzzles as unsolved?")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                puzzlesAdapter.resetAllPuzzlesFinishedAndMove();
                            }
                        });
                dialog.show();
                return true;
            }
        });
    }
}
