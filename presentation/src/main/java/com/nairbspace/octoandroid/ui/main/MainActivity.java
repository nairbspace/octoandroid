package com.nairbspace.octoandroid.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.BaseActivity;
import com.nairbspace.octoandroid.ui.Presenter;
import com.nairbspace.octoandroid.ui.connection.ConnectionFragment;
import com.nairbspace.octoandroid.ui.status.StatusFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity<MainScreen>
        implements NavigationView.OnNavigationItemSelectedListener, MainScreen,
        StatusFragment.OnFragmentInteractionListener,
        ConnectionFragment.OnFragmentInteractionListener, View.OnClickListener{

    @Inject MainPresenterTwo mPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.nav_view) NavigationView mNavView;
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    private ActionBarDrawerToggle mToggle;
    private TextView mPrinterNameNavTextView;
    private TextView mPrinterIpAddressNavTextView;

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
//        getNavigator().startWebsocketService(this);
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

        if (requestCode == getNavigator().getAddPrinterRequestCode()) { // TODO need to decide if going to implement this way.
//            mPresenter.getAccounts();
        } // TODO Need response if user decides to not login
    }

    @Override
    public void navigateToAddPrinterActivity() {
//        Intent i = AddPrinterActivity.newIntent(this);
//        startActivityForResult(i, AddPrinterActivity.REQUEST_CODE);
        getNavigator().navigateToAddPrinterActivityForResult(this);
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
    public void updateNavHeader(String printerName, String ipAddress) {
        mPrinterNameNavTextView.setText(printerName);
        mPrinterIpAddressNavTextView.setText(ipAddress);
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
