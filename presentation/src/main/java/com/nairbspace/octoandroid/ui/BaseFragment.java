package com.nairbspace.octoandroid.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nairbspace.octoandroid.data.net.NetworkChecker;
import com.nairbspace.octoandroid.receiver.ActiveNetworkReceiver;
import com.nairbspace.octoandroid.receiver.ActiveNetworkReceiver.ActiveNetworkListener;

import javax.inject.Inject;

import butterknife.Unbinder;

public abstract class BaseFragment<T> extends Fragment implements ActiveNetworkListener {

    private Unbinder mUnbinder;
    private ActiveNetworkReceiver mActiveNetworkReceiver;
    @Inject NetworkChecker mNetworkChecker;

    @NonNull
    protected abstract Presenter setPresenter();

    @NonNull
    protected abstract T setScreen();

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter().onInitialize(setScreen());
        mActiveNetworkReceiver = new ActiveNetworkReceiver(this, mNetworkChecker);
    }

    protected void setActionBarTitle(String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void setUnbinder(Unbinder unbinder) {
        mUnbinder = unbinder;
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
        getActivity().registerReceiver(mActiveNetworkReceiver,
                mActiveNetworkReceiver.getIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        setPresenter().onPause();
        getActivity().unregisterReceiver(mActiveNetworkReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        setPresenter().onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDestroy() {
        super.onDestroy();
        setPresenter().onDestroy(setScreen());
        mActiveNetworkReceiver = null;
    }

    @Override
    public void networkNowActive() {
        setPresenter().networkNowActive();
    }

    @Override
    public void networkNowInactive() {
        setPresenter().networkNowInactive();
    }
}
