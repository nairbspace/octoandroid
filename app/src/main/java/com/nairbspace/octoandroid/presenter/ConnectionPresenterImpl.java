package com.nairbspace.octoandroid.presenter;

import com.nairbspace.octoandroid.interactor.GetConnection;
import com.nairbspace.octoandroid.interactor.GetConnectionImpl;
import com.nairbspace.octoandroid.net.Connection;
import com.nairbspace.octoandroid.ui.ConnectionScreen;

import javax.inject.Inject;

public class ConnectionPresenterImpl implements ConnectionPresenter, GetConnection.GetConnectionFinishedListener {

    private ConnectionScreen mScreen;
    @Inject GetConnectionImpl mGetConnection;

    @Inject
    public ConnectionPresenterImpl() {

    }

    @Override
    public void onDestroy() {
        mScreen = null;
    }

    @Override
    public void setView(ConnectionScreen screen) {
        mScreen = screen;
    }

    @Override
    public void getConnection() {
        mGetConnection.getConnection(this);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSuccess(Connection connection) {
        mScreen.updateUI(connection);
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onSslFailure() {

    }

    @Override
    public void onApiKeyFailure() {

    }
}
