package com.nairbspace.octoandroid.ui.printer_settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
    @BindView(R.id.add_printer_fab) FloatingActionButton mFab;
    private Snackbar mSnackbar;

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
        mSnackbar = Snackbar.make(mFab, "", Snackbar.LENGTH_SHORT);
        mSnackbar.setCallback(new SnackbarCallback());
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
    public void printerEditClicked(long id, int position) {
        mPresenter.printerEditClicked(id);
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
        mSnackbar.setText(message);
        mSnackbar.show();
    }

    /**
     * FAB in this layout has {@link com.nairbspace.octoandroid.views.ScrollHideBehavior} as behavior.
     * {@link com.nairbspace.octoandroid.views.ScrollHideBehavior} currently cannot extend from
     * {@link android.support.design.widget.FloatingActionButton.Behavior} or it will crash so
     * have to hide FAB instead when Snackbar is shown.
     */
    private final class SnackbarCallback extends Snackbar.Callback {

        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            mFab.show();
        }

        @Override
        public void onShown(Snackbar snackbar) {
            mFab.hide();
        }
    }
}
