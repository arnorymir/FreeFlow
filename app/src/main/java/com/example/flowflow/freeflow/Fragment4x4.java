package com.example.flowflow.freeflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment4x4 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_grid_picker_fragment, container, false);

        Button button = (Button) view.findViewById(R.id.fourByFour);
        button.setText("4x4");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                intent.putExtra("BoardSize", 4);
                startActivity(intent);
            }
        });

        return view;
    }

}




