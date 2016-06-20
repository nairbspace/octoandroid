package com.nairbspace.octoandroid.ui.slicer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nairbspace.octoandroid.ui.slicer.slicing.SlicingFragment;

public class SlicerPagerAdapter extends FragmentPagerAdapter {
    private final String[] mTabTitles;

    public SlicerPagerAdapter(String[] array, FragmentManager fm) {
        super(fm);
        mTabTitles = array;
    }

    @Override
    public Fragment getItem(int position) {
        return SlicingFragment.newInstance();
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
