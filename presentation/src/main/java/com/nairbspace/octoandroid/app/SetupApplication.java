package com.nairbspace.octoandroid.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nairbspace.octoandroid.BuildConfig;
import com.nairbspace.octoandroid.di.components.AppComponent;
import com.nairbspace.octoandroid.di.components.DaggerAppComponent;
import com.nairbspace.octoandroid.di.modules.AppModule;
import com.nairbspace.octoandroid.di.modules.NetworkModule;
import com.nairbspace.octoandroid.di.modules.StorageModule;

import timber.log.Timber;

public class SetupApplication extends Application {

    private AppComponent mAppComponent;

    public static SetupApplication get(Context context) {
        return (SetupApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeTimber();
        initializeLeakCanary();
        initializeInjector();
    }

    private void initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private void initializeLeakCanary() {
//        LeakCanary.install(this);
    }

    private void initializeInjector() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .storageModule(new StorageModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
                return;
            }

            if (t != null) {
                if (priority == Log.WARN) { //TODO-LOW Implement crash analytics during run time
                    return;
                }
                if (priority == Log.ERROR) {
                    return;
                }
            }
        }
    }
}
