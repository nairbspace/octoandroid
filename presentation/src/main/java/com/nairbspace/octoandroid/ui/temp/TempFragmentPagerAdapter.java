package com.nairbspace.octoandroid.ui.temp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nairbspace.octoandroid.ui.temp_graph.TempGraphFragment;

public class TempFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT =  1;
    private final String tabTitles[] = new String[]{"Graph"};

    public TempFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TempGraphFragment.newInstance();
//        if (position == 0) {
//            return ConnectionFragment.newInstance();
//        } else if (position == 1) {
//            return StateFragment.newInstance();
//        } else if (position == 2) {
//            return FilesFragment.newInstance();
//        } else {
//            return TempGraphFragment.newInstance();
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
