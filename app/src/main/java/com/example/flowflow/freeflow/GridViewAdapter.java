package com.example.flowflow.freeflow;


import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
//import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

/*
File:       GridViewAdapter.java
Version:    0.1.0
Date:       November, 2013
License:	GPL v2
Description: Gridview sample for Android
****************************************************************************
Copyright (C) 2013 Radu Motisan  <radu.motisan@gmail.com>

http://www.pocketmagic.net

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
****************************************************************************

*/


public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private List<Button> buttonsList;

    public GridViewAdapter(Context context, List<Button> buttonsList ) {
        this.context = context;
        this.buttonsList = buttonsList;
    }

    public int getCount() {
        return buttonsList.size();
    }

    public Object getItem(int position) {
        return buttonsList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        GridViewItemData itemData = itemList.get(position);
        View v = new GridViewItemView(this.context, itemData );
        v.setOnClickListener((OnClickListener) context);
        return v;
    }
}
