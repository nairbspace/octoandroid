package com.nairbspace.octoandroid.ui.temp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nairbspace.octoandroid.ui.temp_controls.TempControlsFragment;
import com.nairbspace.octoandroid.ui.temp_graph.TempGraphFragment;

public class TempFragmentPagerAdapter extends FragmentPagerAdapter {
    private final String[] mTabTitles;

    public TempFragmentPagerAdapter(String[] array, FragmentManager fm) {
        super(fm);
        mTabTitles = array;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return TempGraphFragment.newInstance();
        } else {
            return TempControlsFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mTabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }
}
