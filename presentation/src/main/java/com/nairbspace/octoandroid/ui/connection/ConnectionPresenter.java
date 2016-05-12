package com.nairbspace.octoandroid.ui.connection;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.nairbspace.octoandroid.domain.interactor.ConnectToPrinter;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetConnectionDetails;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.model.ConnectModel;
import com.nairbspace.octoandroid.model.ConnectionModel;
import com.nairbspace.octoandroid.model.mapper.ModelMapper;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

public class ConnectionPresenter extends UseCasePresenter<ConnectionScreen> {

    private ConnectionScreen mScreen;
    private final GetConnectionDetails mGetConnectionDetails;
    private final ModelMapper mModelMapper;
    private final ConnectToPrinter mConnectToPrinter;

    private boolean mIsFirstTime = true;

    @Inject
    public ConnectionPresenter(GetConnectionDetails getConnectionDetails, ModelMapper modelMapper,
                               ConnectToPrinter connectToPrinter) {
        super(getConnectionDetails);
        mGetConnectionDetails = getConnectionDetails;
        mModelMapper = modelMapper;
        mConnectToPrinter = connectToPrinter;
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
        List<String> printerProfileNames = connection.printerProfileNames();

        mScreen.updateUI(ports, baudrates, printerProfileNames, isNotConnected);

        if(mIsFirstTime) {
            mIsFirstTime = false;
            int defaultPortId = connection.defaultPortId();
            int defaultBaudrateId = connection.defaultBaudrateId();
            int defaultPrinterNameId = connection.defaultPrinterNameId();
            mScreen.updateUiWithDefaults(defaultPortId, defaultBaudrateId, defaultPrinterNameId);
        }
    }

    public void connectButtonClicked(ConnectModel connectModel) {
        mModelMapper.transformObs(connectModel).subscribe(mConnectAction);
    }

    private Action1<Connect> mConnectAction = new Action1<Connect>() {
        @Override
        public void call(Connect connect) {
            mConnectToPrinter.setConnect(connect);
            mConnectToPrinter.execute(new ConnectToPrinterSubscriber());
        }
    };

    @RxLogSubscriber
    private final class GetConnectionSubscriber extends DefaultSubscriber<Connection> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(Connection connection) {
            mModelMapper.transformObs(connection).subscribe(mRenderScreen);
        }

        private Action1<ConnectionModel> mRenderScreen = new Action1<ConnectionModel>() {
            @Override
            public void call(ConnectionModel connectionModel2) {
                renderScreen(connectionModel2);
            }
        };
    }

    @RxLogSubscriber
    private final class ConnectToPrinterSubscriber extends DefaultSubscriber<Boolean> {

        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(Boolean aBoolean) {
            if (aBoolean) {
                mGetConnectionDetails.execute(new GetConnectionSubscriber()); // TODO not sure if should create new one
            }
        }
    }
}
