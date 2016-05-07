package com.nairbspace.octoandroid.ui.connection;

import com.nairbspace.octoandroid.data.net.rest.model.Connect;
import com.nairbspace.octoandroid.data.net.rest.model.Connection;
import com.nairbspace.octoandroid.interactor.GetConnection;
import com.nairbspace.octoandroid.interactor.GetConnectionImpl;
import com.nairbspace.octoandroid.ui.Presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ConnectionPresenter extends Presenter<ConnectionScreen> implements
        GetConnection.GetConnectionFinishedListener{

    private ConnectionScreen mScreen;
    private Connection mConnection;
    private boolean ifFirstTime = true;
    private String mClickedCommand;
    @Inject GetConnectionImpl mGetConnection;

    @Inject
    public ConnectionPresenter() {
    }

    @Override
    protected void onInitialize(ConnectionScreen connectionScreen) {
        mScreen = connectionScreen;
        mGetConnection.getConnectionFromDb(this);
    }

    @Override
    protected void isVisibleToUser() {
//        mGetConnection.pollConnection(this);
    }

    @Override
    protected void isNotVisibleToUser() {
        mGetConnection.unsubscribePollConnection();
    }

    public void postConnect(Connect connect) {
        mGetConnection.postConnect(connect, this);
    }

    public void connectButtonClicked(String connectButtonText, int portPosition,
                                     int baudratePosition, int printerProfileNamePosition,
                                     boolean isSaveConnectionSettingsChecked,
                                     boolean isAutoConnectChecked) {

        mClickedCommand = connectButtonText.toLowerCase();

        Connection.Options options = mConnection.options();
        String port = options.ports().get(portPosition);
        int baudrate = options.baudrates().get(baudratePosition);
        String printerProfileId = options.printerProfiles().get(printerProfileNamePosition).id();

        Connect.Builder builder = Connect.builder().command(mClickedCommand);
        if (mClickedCommand.equals(Connect.COMMAND_CONNECT)) {
            builder.port(port)
                    .baudrate(baudrate)
                    .printerProfile(printerProfileId)
                    .save(isSaveConnectionSettingsChecked)
                    .autoconnect(isAutoConnectChecked);
            postConnect(builder.build());
        }
    }

    @Override
    public void onLoading() {
        mScreen.showProgressBar(true);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSuccess(Connection connection) {
        mConnection = connection;

        Connection.Current current = mConnection.current();
        String state = current.state();
        boolean isNotConnected = state.equals("Closed");

        if (mClickedCommand != null) { // TODO Clean up this logic for onRotate
            if (mClickedCommand.equals("connect")) {
                if (!isNotConnected) {
                    mScreen.showProgressBar(false);
                }
            } else {
                if (isNotConnected) {
                    mScreen.showProgressBar(false);
                }
            }
            mClickedCommand = null;
        }

        Connection.Options options = mConnection.options();
        List<String> ports = options.ports();

        List<Integer> baudrates = options.baudrates();

        List<Connection.PrinterProfile> printerProfiles = options.printerProfiles();
        List<String> printerProfileNames = new ArrayList<>();
        for (Connection.PrinterProfile printerProfile : printerProfiles) {
            printerProfileNames.add(printerProfile.name());
        }
        mScreen.updateUI(ports, baudrates, printerProfileNames, isNotConnected);

        if (ifFirstTime) { // TODO need better logic so it doesn't check everytime
            int defaultPortId = 0;
            for (int i = 0; i < ports.size(); i++) {
                if (ports.get(i).equals(options.portPreference())) {
                    defaultPortId = i;
                }
            }

            int defaultBaudrateId = 0;
            for (int i = 0; i < baudrates.size(); i++) {
                if (baudrates.get(i).equals(options.baudratePreference())) {
                    defaultBaudrateId = i;
                }
            }

            int defaultPrinterNameId = 0;
            for(int i =0; i<printerProfileNames.size(); i++) {
                if (printerProfileNames.get(i).equals(options.printerProfilePreference())) {
                    defaultPrinterNameId = i;
                }
            }

            mScreen.updateUiWithDefaults(defaultPortId, defaultBaudrateId, defaultPrinterNameId);
            ifFirstTime = false;
        }
    }

    @Override
    public void onDbSuccess(Connection connection) {
        if (connection != null) {
            onSuccess(connection);
        }
    }

    @Override
    public void onFailure() {
        mScreen.showProgressBar(false);
    }

    @Override
    public void onSslFailure() {

    }

    @Override
    public void onApiKeyFailure() {

    }
}