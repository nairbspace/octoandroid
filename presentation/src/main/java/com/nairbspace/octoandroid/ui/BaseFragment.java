package com.nairbspace.octoandroid.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment<T> extends Fragment {

    @NonNull
    protected abstract Presenter setPresenter();

    @NonNull
    protected abstract T setScreen();

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter().onInitialize(setScreen());
    }

    @Override
    public void onStart() {
        super.onStart();
        setPresenter().onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        setPresenter().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        setPresenter().onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        setPresenter().onStop();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDestroy() {
        super.onDestroy();
        setPresenter().onDestroy(setScreen());
    }
}
