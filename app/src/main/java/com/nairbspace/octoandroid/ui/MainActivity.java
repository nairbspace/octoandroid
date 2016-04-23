package com.nairbspace.octoandroid.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
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

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainScreen {

    @Inject MainPresenterImpl mMainPresenter;

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.fab) FloatingActionButton mFab;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawer;
    @Bind(R.id.nav_view) NavigationView mNavView;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(this).getAppComponent().inject(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mNavView.setNavigationItemSelectedListener(this);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mToggle);

        if (isTabletAndLandscape()) {
            lockDrawer();
            hideIndicator();
        } else {
            unlockDrawer();
        }

        mMainPresenter.setView(this);

        /** Prevent being checked twice if user rotates screen in AddPrinterActivity */
        if (savedInstanceState == null) {
            mMainPresenter.getAccounts();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        closeDrawer(); // Not able to close during onCreate and screen is rotated
        syncToggleState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mToggle != null) {
            mToggle.onConfigurationChanged(newConfig);
        }
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
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
        Intent i = AddPrinterActivity.newIntent(this, null, null);
        startActivityForResult(i, AddPrinterActivity.REQUEST_CODE);
    }

    @Override
    public void displaySnackBar(String message) {
        Snackbar.make(mFab, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void unlockDrawer() {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}
