package com.nairbspace.octoandroid.di.components;

import com.nairbspace.octoandroid.ui.AddPrinterActivity;
import com.nairbspace.octoandroid.di.modules.AppModule;
import com.nairbspace.octoandroid.di.modules.NetworkModule;
import com.nairbspace.octoandroid.di.modules.StorageModule;
import com.nairbspace.octoandroid.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, StorageModule.class})
public interface AppComponent {
    void inject(AddPrinterActivity addPrinterActivity);

    void inject(MainActivity mainActivity);
}
