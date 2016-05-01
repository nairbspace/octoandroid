package com.nairbspace.octoandroid.presenter;

import com.nairbspace.octoandroid.net.Connect;
import com.nairbspace.octoandroid.ui.ConnectionScreen;

public interface ConnectionPresenter {

    void onDestroy();

    void setView(ConnectionScreen screen);

    void getConnection();

    void postConnect(Connect connect);

    void connectButtonClicked(String connectButtonText, int portPosition, int baudratePosition,
                              int printerProfileNamePosition, boolean isSaveConnectionSettingsChecked,
                              boolean isAutoConnectChecked);
}
