package com.nairbspace.octoandroid.ui.connection;

import com.nairbspace.octoandroid.domain.interactor.ConnectToPrinter;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetConnectionDetails;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.mapper.ConnectModelMapper;
import com.nairbspace.octoandroid.mapper.ConnectionMapper;
import com.nairbspace.octoandroid.model.ConnectModel;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.UseCaseEventPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class ConnectionPresenter extends UseCaseEventPresenter<ConnectionScreen, WebsocketModel> {

    private ConnectionScreen mScreen;
    private final GetConnectionDetails mGetConnectionDetails;
    private final ConnectToPrinter mConnectToPrinter;
    private final ConnectionMapper mConnectionMapper;
    private final ConnectModelMapper mConnectModelMapper;
    private boolean mIsFirstTime = true;
    private Boolean mOldClosedOrError;

    @Inject
    public ConnectionPresenter(GetConnectionDetails getConnectionDetails,
                               ConnectToPrinter connectToPrinter,
                               ConnectionMapper connectionMapper,
                               ConnectModelMapper connectModelMapper,
                               EventBus eventBus) {
        super(getConnectionDetails, eventBus);
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
        unsubscribeAll();
        mGetConnectionDetails.execute(new GetConnectionSubscriber());
    }

    @Override
    protected void onNetworkSwitched() {
        execute();
    }

    @Override
    protected void networkNowInactive() {
        mScreen.showErrorView();
    }

    public void connectButtonClicked(ConnectModel connectModel) {
        mScreen.showProgressBar(true);
        mConnectModelMapper.execute(new TransformSubscriber(), connectModel);
    }

    private void renderScreen(ConnectModel connectModel) {
        mScreen.showProgressBar(false);
        mScreen.updateUi(connectModel);

        if(mIsFirstTime) {
            mIsFirstTime = false;
            int defaultPortId = connectModel.selectedPortId();
            int defaultBaudrateId = connectModel.selectedBaudrateId();
            int defaultPrinterProfileId = connectModel.selectedPrinterProfileId();
            mScreen.updateUiWithDefaults(defaultPortId, defaultBaudrateId, defaultPrinterProfileId);
        }
    }

    @Override
    protected void onEvent(WebsocketModel websocketModel) {
        if (mOldClosedOrError == null) { // Initializes Boolean
            mOldClosedOrError = websocketModel.closedOrError();
            return;
        }

        // If doesn't equal old closedOrError value, that means it changed.
        if (mOldClosedOrError != websocketModel.closedOrError()) {
            mOldClosedOrError = websocketModel.closedOrError();
            execute();
        }
    }

    private final class GetConnectionSubscriber extends DefaultSubscriber<Connection> {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mScreen.showErrorView();
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
            mScreen.showErrorView();
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
            mScreen.showErrorView();
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
            mScreen.showErrorView();
        }

        @Override
        public void onNext(Object o) {
            execute();
        }
    }

    @Override
    protected void onDestroy(ConnectionScreen connectionScreen) {
        super.onDestroy(connectionScreen);
        unsubscribeAll();
        mIsFirstTime = true;
    }

    private void unsubscribeAll() {
        mGetConnectionDetails.unsubscribe();
        mConnectToPrinter.unsubscribe();
        mConnectionMapper.unsubscribe();
        mConnectModelMapper.unsubscribe();
    }
}
