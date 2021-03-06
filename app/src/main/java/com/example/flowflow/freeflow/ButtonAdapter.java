package com.example.flowflow.freeflow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;


public class ButtonAdapter extends BaseAdapter {
    private Context mContext;
    private int mCount;
    private int mSize;
    private SoundEffects soundEffects = SoundEffects.getInstance();

    private PuzzleRepo puzzleRepo = PuzzleRepo.getInstance();
    private DbHelper dbHelper;

    public ButtonAdapter(Context c,int count, int size) {
        soundEffects.loadSounds(c);
        mContext = c;
        mCount = count;
        mSize = size;
        dbHelper = new DbHelper( c );
    }

    public int getCount() {
        return mCount;
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
            button.setLayoutParams(new GridView.LayoutParams(200, 200));
            button.setPadding(10, 10, 10, 10);
            switch (mSize) {
                case 5:
                    button.setId(position);
                    break;
                case 6:
                    button.setId(position + puzzleRepo.sizeOf5x5);
                    break;
                case 7:
                    button.setId(position + puzzleRepo.sizeOf5x5 + puzzleRepo.sizeOf6x6);
                    break;
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    soundEffects.sp.play(SoundEffects.soundIds[2], 1, 1, 1, 0, 1);

                    Intent intent = new Intent(mContext, PlayActivity.class);
                    intent.putExtra("Id", v.getId());
                    mContext.startActivity(intent);
                }
            });

        } else {
            button = (Button) convertView;
        }
        if (dbHelper.isFinished(button.getId())) {
            button.setBackgroundColor(Color.GREEN);
        }
        else {
            button.setBackgroundColor(Color.RED);
         }
         button.setText(Integer.toString(position + 1));

        return button;
    }

}