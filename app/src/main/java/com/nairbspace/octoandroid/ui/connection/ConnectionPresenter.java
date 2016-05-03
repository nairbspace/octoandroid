package com.nairbspace.octoandroid.ui.connection;

import com.nairbspace.octoandroid.net.model.Connect;

public interface ConnectionPresenter {

    void onDestroy();

    void isVisible();

    void isNotVisible();

    void setView(ConnectionScreen screen);

    void getData();

    void postConnect(Connect connect);

    void connectButtonClicked(String connectButtonText, int portPosition, int baudratePosition,
                              int printerProfileNamePosition, boolean isSaveConnectionSettingsChecked,
                              boolean isAutoConnectChecked);
}
