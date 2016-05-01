package com.nairbspace.octoandroid.presenter;

import com.nairbspace.octoandroid.interactor.GetAccountsImpl;
import com.nairbspace.octoandroid.interactor.GetConnection;
import com.nairbspace.octoandroid.interactor.GetConnectionImpl;
import com.nairbspace.octoandroid.net.Connect;
import com.nairbspace.octoandroid.net.Connection;
import com.nairbspace.octoandroid.ui.ConnectionScreen;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class ConnectionPresenterImpl implements ConnectionPresenter,
        GetConnection.GetConnectionFinishedListener{

    private ConnectionScreen mScreen;
    private Connection mConnection;
    @Inject GetConnectionImpl mGetConnection;
    @Inject GetAccountsImpl mGetAccounts;

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
    public void postConnect(Connect connect) {
        Timber.d(connect.toString());
        mGetConnection.postConnect(connect);
    }

    @Override
    public void connectButtonClicked(String connectButtonText, int portPosition,
                                     int baudratePosition, int printerProfileNamePosition,
                                     boolean isSaveConnectionSettingsChecked,
                                     boolean isAutoConnectChecked) {

        String command = connectButtonText.toLowerCase();

        Connection.Options options = mConnection.getOptions();
        String port = options.getPorts().get(portPosition);
        int baudrate = options.getBaudrates().get(baudratePosition);
        String printerProfileId = options.getPrinterProfiles().get(printerProfileNamePosition).getId();

        Connect connect = new Connect(command, port, baudrate,
                printerProfileId, isSaveConnectionSettingsChecked, isAutoConnectChecked);

        postConnect(connect);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSuccess(Connection connection) {
        mConnection = connection;
        Connection.Options options = mConnection.getOptions();
        List<String> ports = options.getPorts();

        List<Integer> baudrates = options.getBaudrates();

        List<Connection.PrinterProfile> printerProfiles = options.getPrinterProfiles();
        List<String> printerProfileNames = new ArrayList<>();
        for (Connection.PrinterProfile printerProfile : printerProfiles) {
            printerProfileNames.add(printerProfile.getName());
        }
        mScreen.updateUI(ports, baudrates, printerProfileNames);
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
