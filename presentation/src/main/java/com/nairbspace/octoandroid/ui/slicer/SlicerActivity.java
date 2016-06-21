package com.nairbspace.octoandroid.ui.slicer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.files.FilesFragment;
import com.nairbspace.octoandroid.ui.playback.PlaybackFragment;
import com.nairbspace.octoandroid.ui.slicer.slicing.SlicingFragment;
import com.nairbspace.octoandroid.ui.templates.BaseNavActivity;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.ButterKnife;

public class SlicerActivity extends BaseNavActivity<SlicerScreen>
        implements SlicerScreen, PlaybackFragment.Listener, SlicingFragment.Listener,
        FilesFragment.Listener {

    private static final String API_URL_KEY = "api_url_key";

    @Inject SlicerPresenter mPresenter;
    @BindArray(R.array.slice_pager_adapter) String[] mPagerArray;

    public static Intent newIntent(Context context) {
        return new Intent(context, SlicerActivity.class);
    }

    public static Intent newIntent(Context context, String apiUrl) {
        Intent intent = new Intent(context, SlicerActivity.class);
        intent.putExtra(API_URL_KEY, apiUrl);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(this).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        onCreateDrawer(ButterKnife.bind(this));

        SlicerPagerAdapter adapter;
        String apiUrl = getIntent().getStringExtra(API_URL_KEY);
        if (apiUrl == null) {
            adapter = new SlicerPagerAdapter(mPagerArray, getSupportFragmentManager());
        } else {
            adapter = new SlicerPagerAdapter(mPagerArray, getSupportFragmentManager(), apiUrl);
        }
        inflateAdapter(adapter);
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

    @Override
    public void sliceButtonClicked(String apiUrl) {
        if (apiUrl != null) sendApiUrl(apiUrl);
    }

    public void sendApiUrl(@NonNull String apiUrl) {
        SlicerPagerAdapter adapter = (SlicerPagerAdapter) getViewPager().getAdapter();
        SlicingFragment slicingFragment = adapter.getSlicingFragment();
        if (slicingFragment == null) return;
        slicingFragment.setApiUrl(apiUrl);
        getViewPager().setCurrentItem(adapter.getSlicingFragmentPosition());
    }
}
