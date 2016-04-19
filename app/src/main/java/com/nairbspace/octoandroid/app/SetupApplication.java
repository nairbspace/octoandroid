package com.nairbspace.octoandroid.app;

import android.app.Application;
import android.support.design.BuildConfig;
import android.util.Log;

import timber.log.Timber;

public class SetupApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
                return;
            }

            if (t != null) {
                if (priority == Log.WARN) { //TODO Implement crash analytics during run time
                    return;
                }
                if (priority == Log.ERROR) {
                    return;
                }
            }
        }
    }
}
