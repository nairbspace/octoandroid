package com.nairbspace.octoandroid.di.components;

import com.nairbspace.octoandroid.account_manager.Authenticator;
import com.nairbspace.octoandroid.di.modules.AppModule;
import com.nairbspace.octoandroid.di.modules.NetworkModule;
import com.nairbspace.octoandroid.di.modules.RestModule;
import com.nairbspace.octoandroid.di.modules.StorageModule;
import com.nairbspace.octoandroid.di.modules.WebsocketModule;
import com.nairbspace.octoandroid.ui.add_printer.AddPrinterActivity;
import com.nairbspace.octoandroid.ui.connection.ConnectionFragment;
import com.nairbspace.octoandroid.ui.files.FilesFragment;
import com.nairbspace.octoandroid.ui.main.MainActivity;
import com.nairbspace.octoandroid.ui.playback.PlaybackFragment;
import com.nairbspace.octoandroid.ui.status.StatusFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, StorageModule.class,
        RestModule.class, WebsocketModule.class})
public interface AppComponent {
    void inject(AddPrinterActivity addPrinterActivity);

    void inject(MainActivity mainActivity);

    void inject(Authenticator authenticator);

    void inject(ConnectionFragment connectionFragment);

    void inject(StatusFragment statusFragment);

    void inject(FilesFragment filesFragment);

    void inject(PlaybackFragment playbackFragment);
}
