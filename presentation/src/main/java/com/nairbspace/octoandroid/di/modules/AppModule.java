package com.nairbspace.octoandroid.di.modules;

import android.app.Application;
import android.content.Context;

import com.nairbspace.octoandroid.data.disk.DiskManager;
import com.nairbspace.octoandroid.data.disk.DiskManagerImpl;
import com.nairbspace.octoandroid.data.executor.JobExecutor;
import com.nairbspace.octoandroid.data.repository.PrinterDataRepository;
import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;
import com.nairbspace.octoandroid.ui.UiThread;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UiThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    DiskManager providePrinterCache(DiskManagerImpl printerCache) {
        return printerCache;
    }

    @Provides
    @Singleton
    PrinterRepository providePrinterRepository(PrinterDataRepository printerDataRepository) {
        return printerDataRepository;
    }
}
