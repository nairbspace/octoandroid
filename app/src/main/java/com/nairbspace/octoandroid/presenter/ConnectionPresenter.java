package com.nairbspace.octoandroid.presenter;

import com.nairbspace.octoandroid.ui.ConnectionScreen;

public interface ConnectionPresenter {

    void onDestroy();

    void setView(ConnectionScreen screen);

    void getConnection();
}
