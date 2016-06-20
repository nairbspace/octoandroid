package com.nairbspace.octoandroid.ui.slicer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.playback.PlaybackFragment;
import com.nairbspace.octoandroid.ui.slicer.slicing.SlicingFragment;
import com.nairbspace.octoandroid.ui.templates.BaseNavActivity;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.ButterKnife;

public class SlicerActivity extends BaseNavActivity<SlicerScreen>
        implements SlicerScreen, PlaybackFragment.Listener, SlicingFragment.Listener {

    @Inject SlicerPresenter mPresenter;
    @BindArray(R.array.slice_pager_adapter) String[] mPagerArray;

    public static Intent newIntent(Context context) {
        return new Intent(context, SlicerActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(this).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        onCreateDrawer(ButterKnife.bind(this));
        inflateAdapter(new SlicerPagerAdapter(mPagerArray, getSupportFragmentManager()));
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected SlicerScreen setScreen() {
        return this;
    }
}
