package com.example.flowflow.freeflow;

import android.content.Intent;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class GridPickerActivity extends FragmentActivity {
    ViewPager Tab;
    TabPagerAdapter TabAdapter;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // These buttons are here temporarily to allow us to get to the PlayActivity

        setContentView(R.layout.activity_grid_picker);

        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
        Tab = (ViewPager) findViewById(R.id.pager);
        Tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar = getActionBar();
                        actionBar.setSelectedNavigationItem(position);
                    }
                });


        Tab.setAdapter(TabAdapter);
        actionBar = getActionBar();
        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabReselected(android.app.ActionBar.Tab tab,
                                        FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                Tab.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(android.app.ActionBar.Tab tab,
                                        FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }
        };
        //Add New Tab
        actionBar.addTab(actionBar.newTab().setText("4x4").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("5x5").setTabListener(tabListener));

    }


}