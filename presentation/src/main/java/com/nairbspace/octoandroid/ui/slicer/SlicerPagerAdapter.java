package com.nairbspace.octoandroid.ui.slicer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.nairbspace.octoandroid.ui.files.FilesFragment;
import com.nairbspace.octoandroid.ui.slicer.slicing.SlicingFragment;

public class SlicerPagerAdapter extends FragmentPagerAdapter {
    SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();
    private final String[] mTabTitles;
    private String mApiUrl;

    public SlicerPagerAdapter(String[] array, FragmentManager fm) {
        super(fm);
        mTabTitles = array;
    }

    public SlicerPagerAdapter(String[] array, FragmentManager fm, String apiUrl) {
        super(fm);
        mTabTitles = array;
        mApiUrl = apiUrl;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            if (mApiUrl == null) return SlicingFragment.newInstance();
            else return SlicingFragment.newInstance(mApiUrl);
        } else {
            return FilesFragment.newInstance();
        }
    }

    public SlicingFragment getSlicingFragment() {
        return (SlicingFragment) mRegisteredFragments.get(getSlicingFragmentPosition());
    }

    public int getSlicingFragmentPosition() {
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
