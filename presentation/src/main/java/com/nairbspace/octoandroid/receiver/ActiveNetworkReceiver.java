package com.nairbspace.octoandroid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.nairbspace.octoandroid.data.net.NetworkChecker;

public class ActiveNetworkReceiver extends BroadcastReceiver {
    private static final String NETWORK_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private final ActiveNetworkListener mListener;
    private final NetworkChecker mNetworkChecker;

    public ActiveNetworkReceiver(ActiveNetworkListener listener, NetworkChecker networkChecker) {
        mListener = listener;
        mNetworkChecker = networkChecker;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mNetworkChecker.isNetworkConnected()) {
            mListener.networkNowActive();
        } else {
            mListener.networkNowInactive();
        }
    }

    public IntentFilter getIntentFilter() {
        return new IntentFilter(NETWORK_ACTION);
    }

    public interface ActiveNetworkListener {
        void networkNowActive();
        void networkNowInactive();
    }
}
