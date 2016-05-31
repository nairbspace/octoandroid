package com.nairbspace.octoandroid.ui.templates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.ui.playback.PlaybackFragment;
import com.nairbspace.octoandroid.ui.status.StatusFragmentPagerAdapter;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public abstract class BaseNavigatorActivity<T> extends BaseActivity<T>
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.nav_view) NavigationView mNavView;
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.fragment_controls) CardView mPlaybackView;
    @BindBool(R.bool.is_tablet_and_landscape) boolean mIsTabletAndLandScape;
    private ActionBarDrawerToggle mToggle;
    private TextView mPrinterNameNavTextView; // TODO class will need its own presenter
    private TextView mPrinterIpAddressNavTextView;

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFab.setOnClickListener(mFabClickListener);
        mNavView.setNavigationItemSelectedListener(this);
        View navHeaderView = mNavView.getHeaderView(0);
        mPrinterNameNavTextView = ButterKnife.findById(navHeaderView, R.id.printer_name_nav_textview);
        mPrinterIpAddressNavTextView = ButterKnife.findById(navHeaderView, R.id.printer_ip_address_nav_textview);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mToggle);
        setDrawer();

        mFragmentManager = getSupportFragmentManager();
        inflatePlaybackFragment();
    }

    private void inflatePlaybackFragment() {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragment_controls);
        if (fragment == null) {
            fragment = PlaybackFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_controls, fragment)
                    .commit();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        closeDrawer();
        syncToggleState();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // TODO not sure if should implement margin this way...
        int bottomMargin = mPlaybackView.getHeight();
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) mViewPager.getLayoutParams();
        p.setMargins(p.leftMargin, p.topMargin, p.rightMargin, bottomMargin);
        mViewPager.setLayoutParams(p);
    }

    private void closeDrawer() {
        if (!mIsTabletAndLandScape) { // Cannot close drawer if in tablet landscape
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }

    private boolean isDrawerOpen() {
        return mDrawer.isDrawerOpen(GravityCompat.START);
    }

    private void lockDrawer(boolean shouldLock) {
        if (shouldLock) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        } else {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    private void hideIndicator() {
        if (mToggle != null) {
            mToggle.setDrawerIndicatorEnabled(false);
        }
    }

    private void syncToggleState() {
        if (mToggle != null) {
            mToggle.syncState();
        }
    }

    private void setDrawer() {
        if (mIsTabletAndLandScape) {
            lockDrawer(true);
            hideIndicator();
        } else {
            lockDrawer(false);
        }
    }

    private View.OnClickListener mFabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(mFab, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }
    };

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_status:
                inflateStatusAdapter();
                break;
            case R.id.nav_webcam:
                getNavigator().navigateToWebcam(this);
                break;
        }
        closeDrawer(); // TODO might to need to close first to simulate one activity
        return true;
    }

    private void setAdapterAndTabLayout(PagerAdapter pagerAdapter) {
        if (mViewPager != null) {
            mViewPager.setAdapter(pagerAdapter);
            mViewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        }

        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    private void inflateAdapter(PagerAdapter pagerAdapter) {
        if (mViewPager.getAdapter() == null) {
            setAdapterAndTabLayout(pagerAdapter);
        } else {
            PagerAdapter currentAdapter = mViewPager.getAdapter();
            Class adapterClass = currentAdapter.getClass();
            if (!adapterClass.equals(pagerAdapter.getClass())) {
                setAdapterAndTabLayout(pagerAdapter);
            } else {
                String message = pagerAdapter.getClass().getName() + " is already visible";
                Timber.d(message);
            }
        }
    }

    protected void inflateStatusAdapter() {
        inflateAdapter(new StatusFragmentPagerAdapter(mFragmentManager));
        mNavView.setCheckedItem(R.id.nav_status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                getNavigator().navigateToSettingsActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
