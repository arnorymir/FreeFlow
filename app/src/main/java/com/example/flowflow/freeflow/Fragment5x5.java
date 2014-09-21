package com.example.flowflow.freeflow;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


public class Fragment5x5 extends Fragment {

    private PuzzleRepo test = PuzzleRepo.getInstance();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_grid_picker_fragment, container, false);
        Button button = (Button) view.findViewById(R.id.gridPickerButton);

        //assuming you have a friendsView object that is some sort of Layout.
        Button button1 = new Button(getActivity());
        //do stuff like add text and listeners.


        for (int i = 0; i < test.mPuzzles.size(); i++) {

            /*
            layout.setOrientation(LinearLayout.VERTICAL);
            Button btn = new Button(this);
            btn.setText("    ");
            layout.addView(btn);*/
        }



        button.setText("5x5");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), PlayActivity.class);
                intent.putExtra("BoardSize", 5);

                startActivity(intent);
            }
        });

        return view;
    }


}




