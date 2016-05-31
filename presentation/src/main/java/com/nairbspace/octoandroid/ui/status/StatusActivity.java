package com.nairbspace.octoandroid.ui.status;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.connection.ConnectionFragment;
import com.nairbspace.octoandroid.ui.files.FilesFragment;
import com.nairbspace.octoandroid.ui.playback.PlaybackFragment;
import com.nairbspace.octoandroid.ui.state.StateFragment;
import com.nairbspace.octoandroid.ui.templates.BaseNavActivity;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusActivity extends BaseNavActivity<StatusScreen>
        implements NavigationView.OnNavigationItemSelectedListener, StatusScreen,
        StateFragment.Listener, ConnectionFragment.Listener,
        FilesFragment.Listener, PlaybackFragment.Listener {

    @Inject StatusPresenter mPresenter;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    public static Intent newIntent(Context context) {
        return new Intent(context, StatusActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetupApplication.get(this).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        setUnbinder(ButterKnife.bind(this));
        setSupportActionBar(mToolbar);
        super.onCreate(savedInstanceState);
        inflateAdapter(new StatusFragmentPagerAdapter(getSupportFragmentManager()));
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected StatusScreen setScreen() {
        return this;
    }
}
