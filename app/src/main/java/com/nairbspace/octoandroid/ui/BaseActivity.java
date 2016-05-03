package com.nairbspace.octoandroid.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<T> extends AppCompatActivity {

    @NonNull protected abstract Presenter setPresenter();

    @NonNull protected abstract T setScreen();

    @Override
    protected void onStart() {
        super.onStart();
        setPresenter().onStart();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setPresenter().onInitialize(this);
    }

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

    @SuppressWarnings("unchecked")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setPresenter().onDestroy(setScreen());
    }
}
