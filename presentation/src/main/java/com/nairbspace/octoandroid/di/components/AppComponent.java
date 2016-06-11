package com.nairbspace.octoandroid.di.components;

import com.nairbspace.octoandroid.account_manager.Authenticator;
import com.nairbspace.octoandroid.di.modules.AppModule;
import com.nairbspace.octoandroid.di.modules.NetworkModule;
import com.nairbspace.octoandroid.di.modules.RestModule;
import com.nairbspace.octoandroid.di.modules.StorageModule;
import com.nairbspace.octoandroid.di.modules.WebsocketModule;
import com.nairbspace.octoandroid.ui.add_printer.AddPrinterActivity;
import com.nairbspace.octoandroid.ui.connection.ConnectionFragment;
import com.nairbspace.octoandroid.ui.dispatch.DispatchActivity;
import com.nairbspace.octoandroid.ui.files.FilesFragment;
import com.nairbspace.octoandroid.ui.print_head.PrintHeadFragment;
import com.nairbspace.octoandroid.ui.printer_controls.PrinterControlsActivity;
import com.nairbspace.octoandroid.ui.printer_controls_general.GeneralFragment;
import com.nairbspace.octoandroid.ui.printer_controls_tool.ToolFragment;
import com.nairbspace.octoandroid.ui.printer_settings.PrinterSettingsActivity;
import com.nairbspace.octoandroid.ui.printer_settings.details.PrinterDetailsFragment;
import com.nairbspace.octoandroid.ui.printer_settings.list.PrinterListFragment;
import com.nairbspace.octoandroid.ui.status.StatusActivity;
import com.nairbspace.octoandroid.ui.playback.PlaybackFragment;
import com.nairbspace.octoandroid.ui.state.StateFragment;
import com.nairbspace.octoandroid.ui.temp.TempActivity;
import com.nairbspace.octoandroid.ui.temp_controls.TempControlsFragment;
import com.nairbspace.octoandroid.ui.temp_graph.TempGraphFragment;
import com.nairbspace.octoandroid.ui.webcam.WebcamActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, StorageModule.class,
        RestModule.class, WebsocketModule.class})
public interface AppComponent {
    void inject(DispatchActivity dispatchActivity);

    void inject(AddPrinterActivity addPrinterActivity);

    void inject(StatusActivity statusActivity);

    void inject(Authenticator authenticator);

    void inject(ConnectionFragment connectionFragment);

    void inject(StateFragment stateFragment);

    void inject(FilesFragment filesFragment);

    void inject(PlaybackFragment playbackFragment);

    void inject(WebcamActivity webcamActivity);

    void inject(TempActivity tempActivity);

    void inject(TempGraphFragment tempGraphFragment);

    void inject(TempControlsFragment tempControlsFragment);

    void inject(PrinterControlsActivity printerControlsActivity);

    void inject(PrintHeadFragment printHeadFragment);

    void inject(ToolFragment toolFragment);

    void inject(GeneralFragment generalFragment);

    void inject(PrinterSettingsActivity printerSettingsActivity);

    void inject(PrinterListFragment printerListFragment);

    void inject(PrinterDetailsFragment printerDetailsFragment);
}
