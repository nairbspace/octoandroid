package com.nairbspace.octoandroid.ui.printer_controls;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nairbspace.octoandroid.ui.print_head.PrintHeadFragment;
import com.nairbspace.octoandroid.ui.printer_controls_tool.ToolFragment;

public class PrinterControlsFragmentPagerAdapter extends FragmentPagerAdapter {
    private final String mTabTitles[];

    public PrinterControlsFragmentPagerAdapter(String[] array, FragmentManager fm) {
        super(fm);
        mTabTitles = array;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return PrintHeadFragment.newInstance();
        } else {
            return ToolFragment.newInstance();
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
