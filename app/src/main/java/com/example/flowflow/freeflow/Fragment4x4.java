package com.example.flowflow.freeflow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment4x4 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View android = inflater.inflate(R.layout.activity_grid_picker_4x4, container, false);
        ((TextView)android.findViewById(R.id.fourByFour)).setText("4x4");
        return android;

    }}