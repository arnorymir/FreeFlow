package com.example.flowflow.freeflow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment5x5 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ios = inflater.inflate(R.layout.activity_grid_picker_4x4, container, false);
        ((TextView)ios.findViewById(R.id.fourByFour)).setText("5x5");
        return ios;
    }}