package com.nairbspace.octoandroid.ui.slicer.slicing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.templates.BaseFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SlicingFragment extends BaseFragmentListener<SlicingScreen, SlicingFragment.Listener>
        implements SlicingScreen {

    @Inject
    SlicingPresenter mPresenter;
    private Listener mListener;

    public static SlicingFragment newInstance() {
        return new SlicingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getContext()).getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slicing_settings, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        return view;
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected SlicingScreen setScreen() {
        return this;
    }

    @NonNull
    @Override
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    public interface Listener {

    }
}
