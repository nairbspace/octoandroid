package com.nairbspace.octoandroid.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nairbspace.octoandroid.ui.connection.ConnectionFragment;
import com.nairbspace.octoandroid.ui.status.StatusFragment;

public class StatusFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT =  2;
    private String tabTitles[] = new String[] { "Connection", "State" };

    public StatusFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ConnectionFragment.newInstance(null, null);
        } else {
            return StatusFragment.newInstance(null, null);
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
