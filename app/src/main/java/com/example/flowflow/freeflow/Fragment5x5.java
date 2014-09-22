package com.example.flowflow.freeflow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class Fragment5x5 extends Fragment {

    private PuzzleRepo puzzleRepo = PuzzleRepo.getInstance();


    //LinearLayout parent = (LinearLayout)findViewById(R.id.parent);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_grid_picker_fragment, container, false);
        Context context = getActivity();

        LinearLayout linearLayout = new LinearLayout(context);
        ViewGroup viewGroup = (ViewGroup) view;

        // add new button and listener for every 5x5 puzzle
        for(int i = 0; i < puzzleRepo.sizeOf5x5  ; i++) {
            Button button = new Button(context);
            button.setText(Integer.toString(i+1));
            button.setId(i);
            viewGroup.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PlayActivity.class);
                    intent.putExtra("Id", v.getId());
                    startActivity(intent);
                }
            });
        }

        return view;
    }

}