package com.nairbspace.octoandroid.ui.printer_controls;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nairbspace.octoandroid.ui.print_head.PrintHeadFragment;

public class PrinterControlsFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 1;
    private final String tabTitles[] = new String[]{"Print Head"};

    public PrinterControlsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PrintHeadFragment.newInstance();
//        if (position == 0) {
//            return TempGraphFragment.newInstance();
//        } else {
//            return TempControlsFragment.newInstance();
//        }
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
