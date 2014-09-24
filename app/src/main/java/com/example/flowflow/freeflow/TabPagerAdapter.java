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
      }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new Fragment5x5();
            case 1:
                return new Fragment6x6();
            case 2:
                return new Fragment7x7();
        }
        return null;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 3; //Number of Tabs
    }

}
