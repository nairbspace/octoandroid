package com.nairbspace.octoandroid.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<T> extends AppCompatActivity {

    @NonNull protected abstract Presenter setPresenter();

    @NonNull protected abstract T setScreen();

    @SuppressWarnings("unchecked")
    @Override
    protected void onStart() {
        super.onStart();
        // Cannot call setScreen method on onCreate because view might get updated before being created
        setPresenter().setScreen(setScreen());
        setPresenter().onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPresenter().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setPresenter().onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setPresenter().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setPresenter().onDestroy();
    }
}
