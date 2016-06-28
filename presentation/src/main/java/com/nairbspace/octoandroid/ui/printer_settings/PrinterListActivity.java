package com.nairbspace.octoandroid.ui.printer_settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.PrinterModel;
import com.nairbspace.octoandroid.ui.templates.BaseActivity;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrinterListActivity extends BaseActivity<PrinterListScreen>
        implements PrinterListScreen, PrinterListRvAdapter.Listener {

    @Inject PrinterListPresenter mPresenter;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.printer_list_recyclerview) RecyclerView mRecyclerView;

    private List<PrinterModel> mPrinterModels;

    private boolean mPrinterWasAdded = false;

    public static Intent newIntent(Context context) {
        return new Intent(context, PrinterListActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(this).getAppComponent().inject(this);
        setContentView(R.layout.activity_printer_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setUpArrow(getSupportActionBar());
        mRecyclerView.setAdapter(new PrinterListRvAdapter(this, null));
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

    @Override
    public void navigateToPrinterDetailsActivity() {
        getNavigator().navigateToPrinterDetailsActivity(this);
    }

    @Override
    public void updateUi(List<PrinterModel> printerModels) {
        mPrinterModels = printerModels;
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter == null) {
            mRecyclerView.setAdapter(new PrinterListRvAdapter(this, printerModels));
        } else if (adapter instanceof PrinterListRvAdapter) {
            ((PrinterListRvAdapter) adapter).setPrinterModels(printerModels);
        }
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
            mPresenter.execute();
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
    protected PrinterListScreen setScreen() {
        return this;
    }

    @Override
    public void printerSettingsClicked(int position) {
        mPresenter.printerSettingsClicked(mPrinterModels.get(position));
    }
}
