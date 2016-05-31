package com.nairbspace.octoandroid.ui.status;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nairbspace.octoandroid.ui.connection.ConnectionFragment;
import com.nairbspace.octoandroid.ui.files.FilesFragment;
import com.nairbspace.octoandroid.ui.state.StateFragment;

public class StatusFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT =  3;
    private final String tabTitles[] = new String[]{"Connection", "State", "Files"};

    public StatusFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
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
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
