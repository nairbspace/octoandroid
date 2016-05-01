package com.nairbspace.octoandroid.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.presenter.MainPresenterImpl;
import com.nairbspace.octoandroid.ui.add_printer.AddPrinterActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainScreen,
        StatusFragment.OnFragmentInteractionListener,
        ConnectionFragment.OnFragmentInteractionListener, View.OnClickListener{

    @Inject MainPresenterImpl mMainPresenter;

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.fab) FloatingActionButton mFab;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawer;
    @Bind(R.id.nav_view) NavigationView mNavView;
    @Bind(R.id.view_pager) ViewPager mViewPager;
    @Bind(R.id.tab_layout) TabLayout mTabLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(this).getAppComponent().inject(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mFab.setOnClickListener(this);
        mNavView.setNavigationItemSelectedListener(this);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mToggle);
        setDrawer();
        inflateStatusAdapter();

        mMainPresenter.setView(this);
        mMainPresenter.getAccounts();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == AddPrinterActivity.REQUEST_CODE) {
            mMainPresenter.getAccounts();
        } // TODO Need response if user decides to not login
    }

    @Override
    public void navigateToAddPrinterActivity() {
        Intent i = AddPrinterActivity.newIntent(this);
        startActivityForResult(i, AddPrinterActivity.REQUEST_CODE);
    }

    @Override
    public void displaySnackBar(String message) {
        Snackbar.make(mFab, message, Snackbar.LENGTH_LONG).show();
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
}
