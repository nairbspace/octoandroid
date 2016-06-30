package com.nairbspace.octoandroid.ui.terminal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.playback.PlaybackFragment;
import com.nairbspace.octoandroid.ui.templates.BaseNavActivity;
import com.nairbspace.octoandroid.ui.templates.Presenter;
import com.nairbspace.octoandroid.ui.terminal.console.ConsoleFragment;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.ButterKnife;

public class TerminalActivity extends BaseNavActivity<TerminalScreen>
        implements TerminalScreen, PlaybackFragment.Listener, ConsoleFragment.Listener {

    @Inject TerminalPresenter mPresenter;
    @BindArray(R.array.terminal_pager_adapter) String[] mPagerArray;

    public static Intent newIntent(Context context) {
        return new Intent(context, TerminalActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(this).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        onCreateDrawer(ButterKnife.bind(this));
        FragmentManager fm = getSupportFragmentManager();
        TerminalPagerAdapter adapter = new TerminalPagerAdapter(fm, mPagerArray);
        inflateAdapter(adapter);
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected TerminalScreen setScreen() {
        return this;
    }
}
