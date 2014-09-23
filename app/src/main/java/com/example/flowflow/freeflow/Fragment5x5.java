package com.example.flowflow.freeflow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Fragment5x5 extends Fragment {

    private PuzzleRepo puzzleRepo = PuzzleRepo.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_grid_picker_fragment, container, false);
        Context context = getActivity();

        LinearLayout linearLayout = new LinearLayout(context);
        List<Button> buttonList = new ArrayList<Button>();

        GridView gridView = new GridView(context);
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
         //   buttonList.add(button);
        }
/*
        gridView.setNumColumns(3);


        ArrayAdapter<Button> adp = new ArrayAdapter<Button>(context, buttonList);

        gridView.setAdapter(adp);
        viewGroup.addView(gridView);
*/
        return view;
    }

}