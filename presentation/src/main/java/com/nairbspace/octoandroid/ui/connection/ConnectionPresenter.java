package com.nairbspace.octoandroid.ui.connection;

import com.nairbspace.octoandroid.interactor.GetConnection;
import com.nairbspace.octoandroid.interactor.GetConnectionImpl;
import com.nairbspace.octoandroid.net.rest.model.Connect;
import com.nairbspace.octoandroid.net.rest.model.Connection;
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

        Connection.Options options = mConnection.getOptions();
        String port = options.getPorts().get(portPosition);
        int baudrate = options.getBaudrates().get(baudratePosition);
        String printerProfileId = options.getPrinterProfiles().get(printerProfileNamePosition).getId();

        Connect connect = new Connect(mClickedCommand);
        if (mClickedCommand.equals(Connect.CONNECT)) {
            connect.setPort(port);
            connect.setBaudrate(baudrate);
            connect.setPrinterProfile(printerProfileId);
            connect.setSave(isSaveConnectionSettingsChecked);
            connect.setAutoconnect(isAutoConnectChecked);
        }
        postConnect(connect);
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

        Connection.Current current = mConnection.getCurrent();
        String state = current.getState();
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

        Connection.Options options = mConnection.getOptions();
        List<String> ports = options.getPorts();

        List<Integer> baudrates = options.getBaudrates();

        List<Connection.PrinterProfile> printerProfiles = options.getPrinterProfiles();
        List<String> printerProfileNames = new ArrayList<>();
        for (Connection.PrinterProfile printerProfile : printerProfiles) {
            printerProfileNames.add(printerProfile.getName());
        }
        mScreen.updateUI(ports, baudrates, printerProfileNames, isNotConnected);

        if (ifFirstTime) { // TODO need better logic so it doesn't check everytime
            int defaultPortId = 0;
            for (int i = 0; i < ports.size(); i++) {
                if (ports.get(i).equals(options.getPortPreference())) {
                    defaultPortId = i;
                }
            }

            int defaultBaudrateId = 0;
            for (int i = 0; i < baudrates.size(); i++) {
                if (baudrates.get(i).equals(options.getBaudratePreference())) {
                    defaultBaudrateId = i;
                }
            }

            int defaultPrinterNameId = 0;
            for(int i =0; i<printerProfileNames.size(); i++) {
                if (printerProfileNames.get(i).equals(options.getPrinterProfilePreference())) {
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