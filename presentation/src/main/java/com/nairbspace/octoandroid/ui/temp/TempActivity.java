package com.nairbspace.octoandroid.ui.temp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.playback.PlaybackFragment;
import com.nairbspace.octoandroid.ui.temp_controls.TempControlsFragment;
import com.nairbspace.octoandroid.ui.temp_graph.TempGraphFragment;
import com.nairbspace.octoandroid.ui.templates.BaseNavActivity;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TempActivity extends BaseNavActivity<TempScreen> implements TempScreen,
        PlaybackFragment.Listener, TempGraphFragment.Listener, TempControlsFragment.Listener{

    @Inject TempPresenter mPresenter;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    public static Intent newIntent(Context context) {
        return new Intent(context, TempActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SetupApplication.get(this).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        setUnbinder(ButterKnife.bind(this));
        setSupportActionBar(mToolbar);
        super.onCreate(savedInstanceState);
        inflateAdapter(new TempFragmentPagerAdapter(getSupportFragmentManager()));
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected TempScreen setScreen() {
        return this;
    }
}
