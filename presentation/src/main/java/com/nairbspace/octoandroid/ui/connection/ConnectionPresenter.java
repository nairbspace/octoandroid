package com.nairbspace.octoandroid.ui.connection;

import com.nairbspace.octoandroid.domain.interactor.ConnectToPrinter;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetConnectionDetails;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.mapper.ConnectModelMapper;
import com.nairbspace.octoandroid.mapper.ConnectionMapper;
import com.nairbspace.octoandroid.model.ConnectModel;
import com.nairbspace.octoandroid.ui.templates.UseCasePresenter;

import javax.inject.Inject;

public class ConnectionPresenter extends UseCasePresenter<ConnectionScreen> {

    private ConnectionScreen mScreen;
    private final GetConnectionDetails mGetConnectionDetails;
    private final ConnectToPrinter mConnectToPrinter;
    private final ConnectionMapper mConnectionMapper;
    private final ConnectModelMapper mConnectModelMapper;
    private boolean mIsFirstTime = true;

    @Inject
    public ConnectionPresenter(GetConnectionDetails getConnectionDetails,
                               ConnectToPrinter connectToPrinter,
                               ConnectionMapper connectionMapper,
                               ConnectModelMapper connectModelMapper) {
        super(getConnectionDetails);
        mGetConnectionDetails = getConnectionDetails;
        mConnectToPrinter = connectToPrinter;
        mConnectionMapper = connectionMapper;
        mConnectModelMapper = connectModelMapper;
    }

    @Override
    protected void onInitialize(ConnectionScreen connectionScreen) {
        mScreen = connectionScreen;
        execute();
    }

    @Override
    protected void execute() {
        super.execute();
        mGetConnectionDetails.execute(new GetConnectionSubscriber());
    }

    @Override
    protected void onNetworkSwitched() {
        super.onNetworkSwitched();
        execute();
    }

    @Override
    protected void networkNowInactive() {
        super.networkNowInactive();
        mScreen.enableScreen(false);
    }

    public void connectButtonClicked(ConnectModel connectModel) {
        mScreen.showProgressBar(true);
        mConnectModelMapper.execute(new TransformSubscriber(), connectModel);
    }

    private void renderScreen(ConnectModel connectModel) {
        mScreen.showProgressBar(false);
        mScreen.updateUi(connectModel, true);

        if(mIsFirstTime) {
            mIsFirstTime = false;
            int defaultPortId = connectModel.selectedPortId();
            int defaultBaudrateId = connectModel.selectedBaudrateId();
            int defaultPrinterProfileId = connectModel.selectedPrinterProfileId();
            mScreen.updateUiWithDefaults(defaultPortId, defaultBaudrateId, defaultPrinterProfileId);
        }
    }

    private final class GetConnectionSubscriber extends DefaultSubscriber<Connection> {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mScreen.showProgressBar(false);
        }

        @Override
        public void onNext(Connection connection) {
            mConnectionMapper.execute(new InputMapperSubscriber(), connection);
        }
    }

    private final class InputMapperSubscriber extends DefaultSubscriber<ConnectModel> {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mScreen.showProgressBar(false);
        }

        @Override
        public void onNext(ConnectModel connectModel) {
            renderScreen(connectModel);
        }
    }

    private final class TransformSubscriber extends DefaultSubscriber<Connect> {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mScreen.showProgressBar(false);
        }

        @Override
        public void onNext(Connect connect) {
            mConnectToPrinter.execute(new ConnectToPrinterSubscriber(), connect);
        }
    }

    private final class ConnectToPrinterSubscriber extends DefaultSubscriber {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mScreen.showProgressBar(false);
        }

        @Override
        public void onNext(Object o) {
            mGetConnectionDetails.execute(new GetConnectionSubscriber());
        }
    }

    @Override
    protected void onDestroy(ConnectionScreen connectionScreen) {
        super.onDestroy(connectionScreen);
        mConnectToPrinter.unsubscribe();
        mConnectionMapper.unsubscribe();
        mConnectModelMapper.unsubscribe();
        mIsFirstTime = true;
    }
}
