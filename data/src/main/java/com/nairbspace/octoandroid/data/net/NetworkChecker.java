package com.nairbspace.octoandroid.data.net;

import android.net.ConnectivityManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NetworkChecker {

    private final ConnectivityManager mManager;

    @Inject
    public NetworkChecker(ConnectivityManager manager) {
        mManager = manager;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public Boolean isNetworkConnected() {
        boolean isNetworkAvailable = mManager.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && mManager.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
}
