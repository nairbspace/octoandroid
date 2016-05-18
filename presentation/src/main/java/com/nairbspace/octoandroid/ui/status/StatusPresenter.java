package com.nairbspace.octoandroid.ui.status;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetWebsocket;
import com.nairbspace.octoandroid.domain.model.Websocket;
import com.nairbspace.octoandroid.mapper.WebsocketModelMapper;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

import timber.log.Timber;

public class StatusPresenter extends UseCasePresenter<StatusScreen> {

    private final GetWebsocket mGetWebsocket;
    private final WebsocketModelMapper mMapper;
    private StatusScreen mScreen;

    @Inject
    public StatusPresenter(GetWebsocket getWebsocket, WebsocketModelMapper mapper) {
        super(getWebsocket);
        mGetWebsocket = getWebsocket;
        mMapper = mapper;
    }

    @Override
    protected void onInitialize(StatusScreen statusScreen) {
        mScreen = statusScreen;
//        execute();
    }

    @Override
    protected void networkNowInactive() {
        super.networkNowInactive();
        mGetWebsocket.unsubscribe();
    }

    @Override
    protected void onNetworkSwitched() {
        execute();
    }

    @Override
    protected void execute() {
        mGetWebsocket.execute(new WebsocketSubscriber());
    }

    private final class WebsocketSubscriber extends DefaultSubscriber<Websocket> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Websocket websocket) {
            super.onNext(websocket);
            mMapper.execute(new TransformSubscriber(), websocket);
            if (websocket != null) {
                Timber.d(websocket.toString());
            }
        }
    }

    private final class TransformSubscriber extends DefaultSubscriber<WebsocketModel> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(WebsocketModel websocketModel) {
            super.onNext(websocketModel);
            mScreen.updateUI(websocketModel);
        }
    }

    @Override
    protected void onDestroy(StatusScreen statusScreen) {
        super.onDestroy(statusScreen);
        mMapper.unsubscribe();
    }
}
