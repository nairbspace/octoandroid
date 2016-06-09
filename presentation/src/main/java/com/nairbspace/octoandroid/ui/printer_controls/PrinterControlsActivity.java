package com.nairbspace.octoandroid.ui.printer_controls;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.playback.PlaybackFragment;
import com.nairbspace.octoandroid.ui.print_head.PrintHeadFragment;
import com.nairbspace.octoandroid.ui.templates.BaseNavActivity;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class PrinterControlsActivity extends BaseNavActivity<PrinterControlsScreen>
        implements PrinterControlsScreen, PlaybackFragment.Listener, PrintHeadFragment.Listener {

    @Inject PrinterControlsPresenter mPresenter;

    public static Intent newIntent(Context context) {
        return new Intent(context, PrinterControlsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(this).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        onCreateDrawer(ButterKnife.bind(this));
        inflateAdapter(new PrinterControlsFragmentPagerAdapter(getSupportFragmentManager()));
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected PrinterControlsScreen setScreen() {
        return this;
    }
}
