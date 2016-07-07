package com.nairbspace.octoandroid.ui.templates;

import android.app.Service;
import android.support.annotation.NonNull;

import com.nairbspace.octoandroid.data.net.NetworkChecker;
import com.nairbspace.octoandroid.receiver.ActiveNetworkReceiver;
import com.nairbspace.octoandroid.ui.Navigator;

import javax.inject.Inject;

// TODO-low templates package should be outside of ui package
public abstract class BaseService<T> extends Service implements ActiveNetworkReceiver.ActiveNetworkListener {
    @Inject Navigator mNavigator;
    @Inject NetworkChecker mNetworkChecker;
    private ActiveNetworkReceiver mActiveNetworkReceiver;

    @NonNull protected abstract Presenter setPresenter();
    @NonNull protected abstract T setListener();

    /**
     * If extending off this class super.onCreate() should be called at the end
     */
    @Override
    public void onCreate() {
        //noinspection unchecked
        setPresenter().onInitialize(setListener());
        mActiveNetworkReceiver = new ActiveNetworkReceiver(this, mNetworkChecker);
        registerReceiver(mActiveNetworkReceiver, mActiveNetworkReceiver.getIntentFilter());
    }

    @Override
    public void onDestroy() {
        //noinspection unchecked
        setPresenter().onDestroy(setListener());
        if (mActiveNetworkReceiver != null) {
            unregisterReceiver(mActiveNetworkReceiver);
            mActiveNetworkReceiver = null;
        }
    }

    @Override
    public void networkNowInactive() {
        setPresenter().networkNowActiveReceived();
    }

    @Override
    public void networkNowActive() {
        setPresenter().networkNowInactiveReceived();
    }

    public Navigator getNavigator() {
        return mNavigator;
    }
}
