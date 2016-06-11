package com.nairbspace.octoandroid.ui.printer_settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.PrinterModel;
import com.nairbspace.octoandroid.ui.printer_settings.details.PrinterDetailsFragment;
import com.nairbspace.octoandroid.ui.printer_settings.list.PrinterListFragment;
import com.nairbspace.octoandroid.ui.templates.BaseActivity;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrinterSettingsActivity extends BaseActivity<PrinterSettingsScreen>
        implements PrinterSettingsScreen, PrinterListFragment.Listener, PrinterDetailsFragment.Listener {

    @Inject PrinterSettingsPresenter mPresenter;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private boolean mPrinterWasAdded = false;

    public static Intent newIntent(Context context) {
        return new Intent(context, PrinterSettingsActivity.class);
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

    private void inflatePrinterList() {
        Fragment fragment = PrinterListFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                    .add(R.id.printer_settings_fragment, fragment)
                    .commit();
    }

    @Override
    public void inflatePrinterDetails(PrinterModel printerModel) {
        Fragment fragment = PrinterDetailsFragment.newInstance(printerModel);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.printer_settings_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addPrinter() {
        getNavigator().navigateToAddPrinterActivityForResult(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getNavigator().wasAddPrinterResultOk(requestCode, resultCode)) {
            mPrinterWasAdded = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPrinterWasAdded) {
            mPrinterWasAdded = false;
            inflatePrinterList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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
    protected PrinterSettingsScreen setScreen() {
        return this;
    }
}
