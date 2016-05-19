package com.nairbspace.octoandroid.ui;

public abstract class Presenter<T> {

    private boolean mNetworkWentInactive;

    /**
     * In an activity this is called during onPostCreate to simulate initializing the presenter
     * as the last call of onCreate. Note however in an Activity onPostCreate is called after
     * onStart, so not recommended to use the presenters {@link #onStart()} method. This is not
     * called in onStart since onStart will get called everytime user navigates to an activity
     * unlike onPostCreate.
     *
     * In a fragment this is called during onViewCreated to prevent the presenter from calling any
     * methods that might update the view.
     *
     * @param t Screen interface used for the presenter.
     */
    protected abstract void onInitialize(T t);

    /**
     * Used to receive network active broadcasts. Do not use this class.Might accidently
     * exclude super and forget count for when internet is switched.
     * Use {@link #networkNowActive()} instead.
     */
    protected void networkNowActiveReceived() {
        networkNowActive();
        if (mNetworkWentInactive) {
            mNetworkWentInactive = false;
            onNetworkSwitched();
        }
    }

    /**
     * Used to receive network inactive broadcasts. Do not use this class. Might accidently
     * exclude super and forget count for when internet is switched.
     * Use {@link #networkNowInactive()} instead.
     */
    protected void networkNowInactiveReceived() {
        mNetworkWentInactive = true;
        networkNowInactive();
    }

    protected void networkNowActive() {
    }

    protected void networkNowInactive() {
    }

    protected void onNetworkSwitched() {
    }

    protected void onStart() {
    }

    protected void onResume() {
    }

    /**
     * Used for fragments inside viewpagers due to the way onPause and onResume are called. This
     * is due to the fact that viewpagers can inflate more than one fragment so onResume and onPause
     * may not be called. Use this method to simulate onResume being called on one fragment.
     */
    protected void isVisibleToUser() {

    }

    /**
     * Used for fragments inside viewpagers due to the way onPause and onResume are called. This
     * is due to the fact that viewpagers can inflate more than one fragment so onResume and onPause
     * may not be called. Use this method to simulate onPause being called on one fragment.
     */
    protected void isNotVisibleToUser() {

    }

    protected void onPause() {
    }

    protected void onStop() {
    }

    @SuppressWarnings("UnusedAssignment")
    protected void onDestroy(T t) {
        if (t != null) {
            t = null;
        }
    }
}