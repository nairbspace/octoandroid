package com.nairbspace.octoandroid.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class LifecycleHandler implements Application.ActivityLifecycleCallbacks {

    private static int sResumed;
    private static int sPaused;
    private static int sStarted;
    private static int sStopped;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        sStarted++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        sResumed++;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        sPaused++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        sStopped++;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public static boolean isApplicationVisible() {
        return sStarted > sStopped;
    }

    /**
     * Application may not be visible, but still be in the foreground.
     * Such as transparent notification overlay.
     * @return result
     */
    public static boolean isApplicationInForeground() {
        return sResumed > sPaused;
    }

    public static boolean isApplicationInBackground() {
        return sStarted > 0;
    }
}
