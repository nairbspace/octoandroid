package com.nairbspace.octoandroid.ui.connection;

import com.nairbspace.octoandroid.data.entity.ConnectEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.interactor.GetConnection;
import com.nairbspace.octoandroid.interactor.GetConnectionImpl;
import com.nairbspace.octoandroid.ui.Presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ConnectionPresenter extends Presenter<ConnectionScreen> implements
        GetConnection.GetConnectionFinishedListener{

    private ConnectionScreen mScreen;
    private ConnectionEntity mConnectionEntity;
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

    public void postConnect(ConnectEntity connectEntity) {
        mGetConnection.postConnect(connectEntity, this);
    }

    public void connectButtonClicked(String connectButtonText, int portPosition,
                                     int baudratePosition, int printerProfileNamePosition,
                                     boolean isSaveConnectionSettingsChecked,
                                     boolean isAutoConnectChecked) {

        ConnectionEntity.Options options = mConnectionEntity.options();
        String port = options.ports().get(portPosition);
        int baudrate = options.baudrates().get(baudratePosition);
        String printerProfileId = options.printerProfiles().get(printerProfileNamePosition).id();

        ConnectEntity connectEntity = ConnectEntity.builder()
                .command(connectButtonText.toLowerCase())
                .port(port)
                .baudrate(baudrate)
                .printerProfile(printerProfileId)
                .save(isSaveConnectionSettingsChecked)
                .autoconnect(isAutoConnectChecked)
                .build();
        postConnect(connectEntity);
    }

    @Override
    public void onLoading() {
        mScreen.showProgressBar(true);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onPostComplete(ConnectEntity connectEntity) {
        mScreen.showProgressBar(false);
        if (connectEntity.command().equals(ConnectEntity.COMMAND_DISCONNECT)) {
            mScreen.showConnectScreen(true);
        } else {
            mScreen.showConnectScreen(false);
        }
    }

    @Override
    public void onSuccess(ConnectionEntity connectionEntity) {
        mConnectionEntity = connectionEntity;

        ConnectionEntity.Current current = mConnectionEntity.current();
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

        ConnectionEntity.Options options = mConnectionEntity.options();
        List<String> ports = options.ports();

        List<Integer> baudrates = options.baudrates();

        List<ConnectionEntity.PrinterProfile> printerProfiles = options.printerProfiles();
        List<String> printerProfileNames = new ArrayList<>();
        for (ConnectionEntity.PrinterProfile printerProfile : printerProfiles) {
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
    public void onNoActivePrinter() {
        // Launch add activity
    }

    @Override
    public void onDbSuccess(ConnectionEntity connectionEntity) {
        ConnectionEntity.Current current = connectionEntity.current();
        String state = current.state();
        boolean isNotConnected = state.equals("Closed");

        ConnectionEntity.Options options = connectionEntity.options();
        List<String> ports = options.ports();
        List<Integer> baudrates = options.baudrates();
        List<ConnectionEntity.PrinterProfile> printerProfiles = options.printerProfiles();
        List<String> printerProfileNames = new ArrayList<>();

        for (ConnectionEntity.PrinterProfile printerProfile : printerProfiles) {
            printerProfileNames.add(printerProfile.name());
        }

        mScreen.updateUI(ports, baudrates, printerProfileNames, isNotConnected);

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

        mGetConnection.getConnection(this);

    }

    @Override
    public void onDbFailure() {
        mGetConnection.getConnection(this);
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