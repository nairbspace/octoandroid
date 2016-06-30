package com.nairbspace.octoandroid.ui.terminal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nairbspace.octoandroid.ui.terminal.console.ConsoleFragment;

public class TerminalPagerAdapter extends FragmentPagerAdapter {

    private final String[] mTabTitles;

    public TerminalPagerAdapter(FragmentManager fm, String[] tabTitles) {
        super(fm);
        mTabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return ConsoleFragment.newInstance();
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
