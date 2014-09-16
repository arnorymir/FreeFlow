package com.example.flowflow.freeflow;

/**
 * Created by arnorymir on 16/09/14.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                //Fragement for Android Tab
                return new Fragment4x4();
            case 1:
                //Fragment for Ios Tab
                return new Fragment5x5();
        }
        return null;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 2; //No of Tabs
    }
}
