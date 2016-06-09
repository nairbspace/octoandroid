package com.nairbspace.octoandroid.ui.status;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nairbspace.octoandroid.ui.connection.ConnectionFragment;
import com.nairbspace.octoandroid.ui.files.FilesFragment;
import com.nairbspace.octoandroid.ui.state.StateFragment;

public class StatusFragmentPagerAdapter extends FragmentPagerAdapter {
    private final String[] mTabTitles;

    public StatusFragmentPagerAdapter(String[] array, FragmentManager fm) {
        super(fm);
        mTabTitles = array;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ConnectionFragment.newInstance();
        } else if (position == 1) {
            return StateFragment.newInstance();
        } else {
            return FilesFragment.newInstance();
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
