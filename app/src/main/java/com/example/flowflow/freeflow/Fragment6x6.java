package com.example.flowflow.freeflow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

public class Fragment6x6 extends Fragment {

    private PuzzleRepo puzzleRepo = PuzzleRepo.getInstance();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.activity_grid_picker_fragment, container, false);
            Context context = getActivity();

            GridView gridview = (GridView) view.findViewById(R.id.gridview);
            // the parameter are context, number of puzzles and size of the board.
            gridview.setAdapter(new buttonAdapter(context,puzzleRepo.sizeOf6x6, 6));

            return view;
        }
}