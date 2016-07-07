package com.nairbspace.octoandroid.ui.printer_settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrinterListActivity extends BaseActivity<PrinterListScreen>
        implements PrinterListScreen, PrinterListRvAdapter.Listener, SwipeRefreshLayout.OnRefreshListener {

    @BindString(R.string.exception_printer_edit_failed) String EDIT_FAILED;
    @BindString(R.string.exception_printer_name_exists) String NAME_EXISTS;

    @Inject PrinterListPresenter mPresenter;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.printer_list_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.rainbow_refresh) SwipeRefreshLayout mRefreshLayout;

    private boolean mPrinterWasAdded = false;
    private int mEditPosition = -1;

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
        mRefreshLayout.setOnRefreshListener(this);
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
    public void navigateToPrinterDetailsActivity(int position) {
        mEditPosition = position;
        getNavigator().navigateToPrinterDetailsActivity(this);
    }

    @Override
    public void updateUi(List<PrinterModel> printerModels) {
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPrinterWasAdded) {
            mPrinterWasAdded = false;
            mPresenter.execute();
        }

        if (mEditPosition > -1) {
            mPresenter.verifyEdit(mEditPosition);
            mEditPosition = -1;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateToStatusActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        navigateToStatusActivity();
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
    public void printerEditClicked(long id, int position) {
        mPresenter.printerEditClicked(id, position);
    }

    @Override
    public void printerDeleteClicked(long id, int position) {
        mPresenter.printerDeleteClicked(id, position);
    }

    @Override
    public void deleteFromAdapter(int position) {
        PrinterListRvAdapter adapter = (PrinterListRvAdapter) mRecyclerView.getAdapter();
        adapter.deletePrinterModel(position);
    }

    @Override
    public void printerSetActiveClicked(long id) {
        mPresenter.printerSetActiveClicked(id);
    }

    @Override
    public void navigateToStatusActivity() {
        getNavigator().navigateToStatusActivity(this);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(mToolbar, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showEditFailure() {
        showSnackbar(EDIT_FAILED);
    }

    @Override
    public void showNameExists() {
        showSnackbar(NAME_EXISTS);
    }

    @Override
    public void onRefresh() {
        mPresenter.execute();
    }

    @Override
    public void setRefreshing(boolean enable) {
        mRefreshLayout.setRefreshing(enable);
    }
}
