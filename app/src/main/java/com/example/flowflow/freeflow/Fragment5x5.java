package com.example.flowflow.freeflow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Fragment5x5 extends Fragment  {

    private PuzzleRepo puzzleRepo = PuzzleRepo.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_grid_picker_fragment, container, false);
        Context context = getActivity();

        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new buttonAdapter(context));

        // add new button and listener for every 5x5 puzzle

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.i("h", "ok");
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                intent.putExtra("Id", position+1);
                startActivity(intent);
            }
        });
         //   buttonList.add(button);

/*
        gridView.setNumColumns(3);


        ArrayAdapter<Button> adp = new ArrayAdapter<Button>(context, buttonList);

        gridView.setAdapter(adp);
        viewGroup.addView(gridView);
*/
        return view;
    }

    public class buttonAdapter extends BaseAdapter {
        private Context mContext;

        public buttonAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return puzzleRepo.sizeOf5x5;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            Button button;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                    button = new Button(mContext);
                    button.setLayoutParams(new GridView.LayoutParams(85, 85));
                    button.setPadding(8, 8, 8, 8);

            } else {
                    button = (Button) convertView;
            }
            button.setText(Integer.toString(position + 1));


            return button;
        }

    }

}