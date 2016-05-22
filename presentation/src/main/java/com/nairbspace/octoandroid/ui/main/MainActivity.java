package com.nairbspace.octoandroid.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.BaseActivity;
import com.nairbspace.octoandroid.ui.Presenter;
import com.nairbspace.octoandroid.ui.connection.ConnectionFragment;
import com.nairbspace.octoandroid.ui.files.FilesFragment;
import com.nairbspace.octoandroid.ui.playback.PlaybackFragment;
import com.nairbspace.octoandroid.ui.status.StatusFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity<MainScreen>
        implements NavigationView.OnNavigationItemSelectedListener, MainScreen,
        StatusFragment.Listener,
        ConnectionFragment.ConnectFragmentListener, View.OnClickListener,
        FilesFragment.Listener, PlaybackFragment.Listener {

    @Inject MainPresenter mPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.nav_view) NavigationView mNavView;
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.fragment_controls) CardView mPlaybackView;
    private ActionBarDrawerToggle mToggle;
    private TextView mPrinterNameNavTextView;
    private TextView mPrinterIpAddressNavTextView;
    private Snackbar mSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(this).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mFab.setOnClickListener(this);
        mNavView.setNavigationItemSelectedListener(this);
        View navHeaderView = mNavView.getHeaderView(0);
        mPrinterNameNavTextView = ButterKnife.findById(navHeaderView, R.id.printer_name_nav_textview);
        mPrinterIpAddressNavTextView = ButterKnife.findById(navHeaderView, R.id.printer_ip_address_nav_textview);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mToggle);
        setDrawer();
        inflateStatusAdapter();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_controls);

        if (fragment == null) {
            fragment = PlaybackFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_controls, fragment)
                    .commit();
        }
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        closeDrawer(); // Not able to close during onCreate and screen is rotated
        syncToggleState();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            getNavigator().navigateToSettingsActivity(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_status:
                inflateStatusAdapter();
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
        }

        closeDrawer();
        return true;
    }

    @Override
    public void closeDrawer() {
        if (!isTabletAndLandscape()) { // Cannot close drawer if in tablet landscape
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean isTabletAndLandscape() {
        return getResources().getBoolean(R.bool.is_tablet_and_landscape);
    }

    @Override
    public boolean isDrawerOpen() {
        return mDrawer.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void lockDrawer() {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
    }

    @Override
    public void hideIndicator() {
        if (mToggle != null) {
            mToggle.setDrawerIndicatorEnabled(false);
        }
    }

    @Override
    public void syncToggleState() {
        if (mToggle != null) {
            mToggle.syncState();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == getNavigator().getAddPrinterRequestCode()) {
            if (resultCode == RESULT_OK) {
                mPresenter.execute();
                refreshStatusAdapter();
            } else {
                displaySnackBarAddPrinterFailure();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void navigateToAddPrinterActivityForResult() {
        getNavigator().navigateToAddPrinterActivityForResult(this);
    }

    @Override
    public void displaySnackBarAddPrinterFailure() {
        Snackbar.make(mFab, R.string.no_printer_found, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.add, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToAddPrinterActivityForResult();
                    }
                }).show();
    }

    @Override
    public void displaySnackBar(String message) {
        mSnackbar = Snackbar.make(mFab, message, Snackbar.LENGTH_INDEFINITE);
        mSnackbar.show();
    }

    @Override
    public void hideSnackbar() {
        if (mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }
    }

    @Override
    public void selectStatusNav() {
        mNavView.setCheckedItem(R.id.nav_status);
    }

    @Override
    public void setDrawer() {
        if (isTabletAndLandscape()) {
            lockDrawer();
            hideIndicator();
        } else {
            unlockDrawer();
        }
    }

    @Override
    public void setAdapterAndTabLayout(PagerAdapter pagerAdapter) {
        if (mViewPager != null) {
            mViewPager.setAdapter(pagerAdapter);
        }

        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    @Override
    public void refreshStatusAdapter() {
        if (mViewPager.getAdapter() != null) {
            Timber.d("Set adapter null");
            mViewPager.setAdapter(null);
        }
        inflateStatusAdapter();
    }

    @Override
    public void inflateStatusAdapter() {
        if (mViewPager.getAdapter() == null) {
            PagerAdapter adapter = new StatusFragmentPagerAdapter(getSupportFragmentManager());
            setAdapterAndTabLayout(adapter);
        } else {
            PagerAdapter currentAdapter = mViewPager.getAdapter();
            Class adapterClass = currentAdapter.getClass();
            if (!adapterClass.equals(StatusFragmentPagerAdapter.class)) {
                PagerAdapter adapter = new StatusFragmentPagerAdapter(getSupportFragmentManager());
                setAdapterAndTabLayout(adapter);
            } else {
                Timber.d("StatusFragmentPagerAdapter already visible");
            }
        }
        selectStatusNav();
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
    public void unlockDrawer() {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Snackbar.make(mFab, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
                break;
        }
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected MainScreen setScreen() {
        return this;
    }

}
