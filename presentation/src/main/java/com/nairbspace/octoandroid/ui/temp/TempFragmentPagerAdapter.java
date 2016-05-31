package com.nairbspace.octoandroid.ui.temp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nairbspace.octoandroid.ui.temp_controls.TempControlsFragment;
import com.nairbspace.octoandroid.ui.temp_graph.TempGraphFragment;

public class TempFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 2;
    private final String tabTitles[] = new String[]{"Graph", "Controls"};

    public TempFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
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
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
