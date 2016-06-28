package com.nairbspace.octoandroid.ui.templates;

import android.content.Context;
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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.ui.playback.PlaybackFragment;
import com.nairbspace.octoandroid.ui.printer_controls.PrinterControlsActivity;
import com.nairbspace.octoandroid.ui.printer_controls.PrinterControlsFragmentPagerAdapter;
import com.nairbspace.octoandroid.ui.slicer.SlicerActivity;
import com.nairbspace.octoandroid.ui.slicer.SlicerPagerAdapter;
import com.nairbspace.octoandroid.ui.status.StatusActivity;
import com.nairbspace.octoandroid.ui.status.StatusFragmentPagerAdapter;
import com.nairbspace.octoandroid.ui.temp.TempActivity;
import com.nairbspace.octoandroid.ui.temp.TempFragmentPagerAdapter;
import com.nairbspace.octoandroid.views.LockSwipeViewPager;

import javax.inject.Inject;

import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public abstract class BaseNavActivity<T> extends BaseActivity<T>
        implements NavigationView.OnNavigationItemSelectedListener, NavScreen {

    @BindString(R.string.exception_no_active_printer) String NO_ACTIVE_PRINTER;
    @BindString(R.string.set) String SET;

    @Inject NavPresenter mPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.nav_view) NavigationView mNavView;
    @BindView(R.id.view_pager) LockSwipeViewPager mViewPager;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.fragment_controls) CardView mPlaybackView;
    @BindBool(R.bool.is_tablet_and_landscape) boolean mIsTabletAndLandScape;
    private ActionBarDrawerToggle mToggle;
    private TextView mPrinterNameNavTextView;
    private TextView mPrinterIpAddressNavTextView;
    private ImageView mPrinterSettingsImageView;
    private Snackbar mSnackbar;
    private FragmentManager mFragmentManager;
    private Unbinder mUnbinder;
    private int mFadeIn;
    private int mFadeOut;

    protected void onCreateDrawer(Unbinder unbinder) {
        mUnbinder = unbinder;
        setSupportActionBar(mToolbar);
        mFab.setOnClickListener(mFabClickListener);
        mNavView.setNavigationItemSelectedListener(this);
        View navHeaderView = mNavView.getHeaderView(0);
        mPrinterNameNavTextView = ButterKnife.findById(navHeaderView, R.id.printer_name_nav_textview);
        mPrinterIpAddressNavTextView = ButterKnife.findById(navHeaderView, R.id.printer_ip_address_nav_textview);
        mPrinterSettingsImageView = ButterKnife.findById(navHeaderView, R.id.nav_printer_settings);
        mPrinterSettingsImageView.setOnClickListener(mPrinterSettingsClickListener);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mToggle);
        setDrawer();

        mFadeIn = mIsTabletAndLandScape ? 0 : android.R.anim.fade_in;
        mFadeOut = mIsTabletAndLandScape ? 0 : android.R.anim.fade_out;

        mFragmentManager = getSupportFragmentManager();
        inflatePlaybackFragment();
    }

    private View.OnClickListener mPrinterSettingsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigateToPrinterListActivity();
        }
    };

    private void navigateToPrinterListActivity() {
        getNavigator().navigateToPrinterListActivity(this);
    }

    public LockSwipeViewPager getViewPager() {
        return mViewPager;
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
        mPresenter.onInitialize(this);
    }


    @Override // TODO not sure if should implement margin this way...
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
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
        closeDrawer();
        switch (item.getItemId()) {
            case R.id.nav_status:
                if (!(this instanceof StatusActivity)) {
                    getNavigator().navigateToStatusActivity(this);
                    // TODO need to work on transistion animations
                    overridePendingTransition(mFadeIn, mFadeOut);
                }
                break;
            case R.id.nav_temp:
                if (!(this instanceof TempActivity)) {
                    getNavigator().navigateToTempActivity(this);
                    overridePendingTransition(mFadeIn, mFadeOut);
                }
                break;
            case R.id.nav_controls:
                if (!(this instanceof PrinterControlsActivity)) {
                    getNavigator().navigateToPrinterControlsActivity(this);
                    overridePendingTransition(mFadeIn, mFadeOut);
                }
                break;
            case R.id.nav_slicer:
                if (!(this instanceof SlicerActivity)) {
                    getNavigator().navigateToSlicerActivity(this);
                    overridePendingTransition(mFadeIn, mFadeOut);
                }
                break;
            case R.id.nav_webcam:
                getNavigator().navigateToWebcam(this);
                break;
        }
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

        if (pagerAdapter instanceof StatusFragmentPagerAdapter) {
            mNavView.setCheckedItem(R.id.nav_status);
        } else if (pagerAdapter instanceof TempFragmentPagerAdapter) {
            mNavView.setCheckedItem(R.id.nav_temp);
        } else if (pagerAdapter instanceof PrinterControlsFragmentPagerAdapter) {
            mNavView.setCheckedItem(R.id.nav_controls);
        } else if (pagerAdapter instanceof SlicerPagerAdapter) {
            mNavView.setCheckedItem(R.id.nav_slicer);
        }
    }

    protected void inflateAdapter(PagerAdapter pagerAdapter) {
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

    @Override
    public void networkNowActive() {
        super.networkNowActive();
        mPresenter.networkNowActiveReceived();
    }

    @Override
    public void networkNowInactive() {
        super.networkNowInactive();
        mPresenter.networkNowInactiveReceived();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder = null;
        }
        mPresenter.onDestroy(this);
    }

    @Override
    public void updateNavHeader(String printerName, String ipAddress) {
        mPrinterNameNavTextView.setText(printerName);
        mPrinterIpAddressNavTextView.setText(ipAddress);
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public void hideSnackbar() {
        if (mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }
    }

    @Override
    public void displaySnackBar(String message) {
        mSnackbar = Snackbar.make(mToolbar, message, Snackbar.LENGTH_INDEFINITE);
        mSnackbar.show();
    }

    @Override
    public void displayNoActivePrinterSnackBar() {
        Snackbar.make(mToolbar, NO_ACTIVE_PRINTER, Snackbar.LENGTH_INDEFINITE)
                .setAction(SET, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToPrinterListActivity();
                    }
                })
                .show();
    }
}
