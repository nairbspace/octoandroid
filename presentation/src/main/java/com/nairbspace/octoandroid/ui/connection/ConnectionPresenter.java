package com.nairbspace.octoandroid.ui.connection;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.nairbspace.octoandroid.domain.interactor.ConnectToPrinter;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetConnectionDetails;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.mapper.ConnectModelMapper;
import com.nairbspace.octoandroid.mapper.ConnectionModelMapper;
import com.nairbspace.octoandroid.model.ConnectModel;
import com.nairbspace.octoandroid.model.ConnectionModel;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import java.util.List;

import javax.inject.Inject;

public class ConnectionPresenter extends UseCasePresenter<ConnectionScreen> {

    private ConnectionScreen mScreen;
    private final GetConnectionDetails mGetConnectionDetails;
    private final ConnectToPrinter mConnectToPrinter;
    private final ConnectionModelMapper mConnectionModelMapper;
    private final ConnectModelMapper mConnectModelMapper;
    private boolean mIsFirstTime = true;

    @Inject
    public ConnectionPresenter(GetConnectionDetails getConnectionDetails,
                               ConnectToPrinter connectToPrinter,
                               ConnectionModelMapper connectionModelMapper,
                               ConnectModelMapper connectModelMapper) {
        super(getConnectionDetails);
        mGetConnectionDetails = getConnectionDetails;
        mConnectToPrinter = connectToPrinter;
        mConnectionModelMapper = connectionModelMapper;
        mConnectModelMapper = connectModelMapper;
    }

    @Override
    protected void onInitialize(ConnectionScreen connectionScreen) {
        mScreen = connectionScreen;
        mGetConnectionDetails.execute(new GetConnectionSubscriber());
    }

    private void renderScreen(ConnectionModel connection) {

        //TODO need autoconnect status!
        boolean isNotConnected = connection.isNotConnected();
        List<String> ports = connection.ports();
        List<Integer> baudrates = connection.baudrates();
        List<String> printerProfileIds = connection.printerProfileIds();
        List<String> printerProfileNames = connection.printerProfileNames();

        mScreen.updateUI(ports, baudrates, printerProfileIds, printerProfileNames, isNotConnected);

        if(mIsFirstTime) {
            mIsFirstTime = false;
            int defaultPortId = connection.defaultPortId();
            int defaultBaudrateId = connection.defaultBaudrateId();
            int defaultPrinterNameId = connection.defaultPrinterProfileId();
            mScreen.updateUiWithDefaults(defaultPortId, defaultBaudrateId, defaultPrinterNameId);
        }
    }

    public void connectButtonClicked(ConnectModel connectModel) {
        mConnectModelMapper.execute(new TransformSubscriber(), connectModel);
    }

    @RxLogSubscriber
    private final class GetConnectionSubscriber extends DefaultSubscriber<Connection> {

        @Override
        public void onNext(Connection connection) {
            mConnectionModelMapper.execute(new InputMapperSubscriber(), connection);
        }
    }

    @RxLogSubscriber
    private final class InputMapperSubscriber extends DefaultSubscriber<ConnectionModel> {
        @Override
        public void onNext(ConnectionModel connectionModel) {
            renderScreen(connectionModel);
        }
    }

    @RxLogSubscriber
    private final class TransformSubscriber extends DefaultSubscriber<Connect> {

        @Override
        public void onNext(Connect connect) {
            mConnectToPrinter.execute(new ConnectToPrinterSubscriber(), connect);
        }
    }

    @RxLogSubscriber
    private final class ConnectToPrinterSubscriber extends DefaultSubscriber<Boolean> {

        @Override
        public void onNext(Boolean aBoolean) {
            if (aBoolean) {
                mGetConnectionDetails.execute(new GetConnectionSubscriber()); // TODO not sure if should create new one
            }
        }
    }
}
