package com.nairbspace.octoandroid.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.nairbspace.octoandroid.BuildConfig;
import com.nairbspace.octoandroid.di.components.AppComponent;
import com.nairbspace.octoandroid.di.components.DaggerAppComponent;
import com.nairbspace.octoandroid.di.modules.AppModule;
import com.nairbspace.octoandroid.di.modules.NetworkModule;
import com.nairbspace.octoandroid.di.modules.RestModule;
import com.nairbspace.octoandroid.di.modules.StorageModule;
import com.nairbspace.octoandroid.di.modules.WebsocketModule;
import com.squareup.leakcanary.LeakCanary;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class SetupApplication extends Application {

    private AppComponent mAppComponent;

    public static SetupApplication get(Context context) {
        return (SetupApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeCrashlytics();
        initializeTimber();
        initializeLeakCanary();
        initializeInjector();
        initializeLifecyclerHandler();
    }

    /**
     * The Crashlytics build method creates an Answers instance which currently
     * cannot be disabled. So must wrap in if statement
     * to check if build config so it doesn't get created.
     * All methods of Answers.getInstance must be wrapped in null checks
     * or build config checks unfortunately.
     */
    private void initializeCrashlytics() {
        if (!BuildConfig.DEBUG) {
            CrashlyticsCore core = new CrashlyticsCore.Builder()
                    .disabled(BuildConfig.DEBUG)
                    .build();

            Fabric.with(this, new Crashlytics.Builder().core(core).build());
        }
    }

    private void initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private void initializeLeakCanary() {
        LeakCanary.install(this);
    }

    private void initializeInjector() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .restModule(new RestModule())
                .websocketModule(new WebsocketModule())
                .storageModule(new StorageModule())
                .build();
    }

    private void initializeLifecyclerHandler() {
        registerActivityLifecycleCallbacks(new LifecycleHandler());
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private class CrashReportingTree extends Timber.Tree {
        private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
        private static final String CRASHLYTICS_KEY_TAG = "tag";
        private static final String CRASHLYTICS_KEY_MESSAGE = "message";

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
                return;
            }

            Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority);
            Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag);
            Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message);

            if (t == null) {
                Crashlytics.logException(new Exception(message));
            } else {
                Crashlytics.logException(t);
            }
        }
    }
}
