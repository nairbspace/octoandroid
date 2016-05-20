package com.nairbspace.octoandroid.ui;

import android.content.Context;
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

public abstract class BaseFragmentListener<T, L> extends Fragment implements ActiveNetworkListener {

    @Inject NetworkChecker mNetworkChecker;
    private Unbinder mUnbinder;
    private ActiveNetworkReceiver mActiveNetworkReceiver;
    private L mListener;

    @NonNull
    protected abstract Presenter setPresenter();

    @NonNull
    protected abstract T setScreen();

    @NonNull
    protected abstract L setListener();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = setListener();
    }

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

    @SuppressWarnings("unchecked")
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setPresenter().onDestroy(setScreen());
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mActiveNetworkReceiver != null) {
            mActiveNetworkReceiver = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mListener != null) {
            mListener = null;
        }
    }

    public Navigator getNavigator() {
        try {
            return ((BaseActivity) getActivity()).getNavigator();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    " should extend from BaseActivity");
        }
    }

    @Override
    public void networkNowActive() {
        setPresenter().networkNowActiveReceived();
    }

    @Override
    public void networkNowInactive() {
        setPresenter().networkNowInactiveReceived();
    }
}
