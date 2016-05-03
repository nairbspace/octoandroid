package com.nairbspace.octoandroid.ui;

public interface Presenter<T> {

    void setScreen(T t);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

}

