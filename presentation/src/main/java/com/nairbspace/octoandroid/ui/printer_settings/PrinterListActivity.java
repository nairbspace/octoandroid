package com.nairbspace.octoandroid.ui.printer_settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.PrinterModel;
import com.nairbspace.octoandroid.ui.printer_settings.list.PrinterListFragment;
import com.nairbspace.octoandroid.ui.templates.BaseActivity;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrinterListActivity extends BaseActivity<PrinterListActivityScreen>
        implements PrinterListActivityScreen, PrinterListFragment.Listener {

    @Inject PrinterListActivityPresenter mPresenter;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private boolean mPrinterWasAdded = false;

    public static Intent newIntent(Context context) {
        return new Intent(context, PrinterListActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(this).getAppComponent().inject(this);
        setContentView(R.layout.activity_printer_settings);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setUpArrow(getSupportActionBar());
        inflatePrinterList();
    }

    private void setUpArrow(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @OnClick(R.id.add_printer_fab)
    void addPrinter() {
        getNavigator().navigateToAddPrinterActivityForResult(this);
    }

    private void inflatePrinterList() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.printer_settings_fragment, PrinterListFragment.newInstance())
                .commit();
    }

    @Override
    public void printerSettingsClicked(PrinterModel printerModel) {
        mPresenter.printerSettingsClicked(printerModel);
    }

    @Override
    public void navigateToPrinterDetailsActivity() {
        getNavigator().navigateToPrinterSettingsActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getNavigator().wasAddPrinterResultOk(requestCode, resultCode)) {
            mPrinterWasAdded = true;
        }

        if (requestCode == getNavigator().getPrinterSettingsRequestCode()) {
            // update list
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPrinterWasAdded) {
            mPrinterWasAdded = false;
            updatePrinterList();
        }
    }

    private void updatePrinterList() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.printer_settings_fragment);
        if (fragment == null) {
            inflatePrinterList();
        } else if (fragment instanceof PrinterListFragment) {
            ((PrinterListFragment) fragment).updateList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected PrinterListActivityScreen setScreen() {
        return this;
    }
}
