package com.nairbspace.octoandroid.ui.playback;

import com.nairbspace.octoandroid.model.WebsocketModel;

public interface PlaybackScreen {

    void updateSeekbar(WebsocketModel websocketModel);

    void showPrintingScreen();

    void showPausedScreen();

    void showFileLoadedScreen();

    void showNoFileLoadedScreen();

    int getPrintRestartId();

    int getPausePlayId();

    int getStopId();

    void setWebsocketServiceAndAlarm(boolean shouldStartAlarm);

    boolean isPrinting();
}
