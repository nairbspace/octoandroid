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
import timber.log.Timber;

public class SlicerActivity extends BaseNavActivity<SlicerScreen>
        implements SlicerScreen, PlaybackFragment.Listener, SlicingFragment.Listener,
        FilesFragment.Listener {

    private static final String API_URL_KEY = "api_url_key";

    @Inject SlicerPresenter mPresenter;
    @BindArray(R.array.slice_pager_adapter) String[] mPagerArray;

    public static Intent newIntent(Context context, @Nullable String apiUrl) {
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

        String apiUrl = getIntent().getStringExtra(API_URL_KEY);
        SlicerPagerAdapter adapter = new SlicerPagerAdapter(mPagerArray, getSupportFragmentManager(), apiUrl);
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
        try {
            SlicerPagerAdapter adapter = (SlicerPagerAdapter) getViewPager().getAdapter();
            SlicingFragment slicingFragment = adapter.getSlicingFragment();
            slicingFragment.setApiUrl(apiUrl);
            getViewPager().setCurrentItem(adapter.getSlicingFragmentPosition());
        } catch (ClassCastException | NullPointerException | IndexOutOfBoundsException e) {
            Timber.e(e, null); // Shouldn't happen.
        }
    }

    @Override
    public void updateFiles() {
        try {
            SlicerPagerAdapter adapter = (SlicerPagerAdapter) getViewPager().getAdapter();
            FilesFragment filesFragment = adapter.getFilesFragment();
            filesFragment.onRefresh();
        } catch (ClassCastException | NullPointerException e) {
            Timber.e(e, null); // Shouldn't happen.
        }
    }
}
