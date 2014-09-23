package com.example.flowflow.freeflow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;


public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private List<Button> buttonList;

    public GridViewAdapter(Context context, List<Button> buttonList ) {
        this.context = context;
        this.buttonList = buttonList;
    }

    public int getCount() {
        return buttonList.size();
    }

    public Object getItem(int position) {
        return buttonList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
       /*
        GridViewItemData itemData = buttonList.get(position);
        View v = new GridViewItemView(this.context, itemData );
        v.setOnClickListener((OnClickListener) context);
        */
        return convertView;
    }
}
