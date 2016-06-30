package com.nairbspace.octoandroid.ui.terminal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.nairbspace.octoandroid.ui.terminal.console.ConsoleFragment;

public class TerminalPagerAdapter extends FragmentPagerAdapter {
    SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();
    private final String[] mTabTitles;

    public TerminalPagerAdapter(FragmentManager fm, String[] tabTitles) {
        super(fm);
        mTabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return ConsoleFragment.newInstance();
    }

    public ConsoleFragment getConsoleFragment() {
        return (ConsoleFragment) mRegisteredFragments.get(getConsoleFragmentPosition());
    }

    public int getConsoleFragmentPosition() {
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mRegisteredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mRegisteredFragments.remove(position);
        super.destroyItem(container, position, object);
    }
}
