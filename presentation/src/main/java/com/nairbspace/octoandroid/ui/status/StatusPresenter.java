package com.nairbspace.octoandroid.ui.status;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetWebsocket;
import com.nairbspace.octoandroid.domain.model.Websocket;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

import timber.log.Timber;

public class StatusPresenter extends UseCasePresenter<StatusScreen> {

    private final GetWebsocket mGetWebsocket;
    private StatusScreen mScreen;

    @Inject
    public StatusPresenter(GetWebsocket getWebsocket) {
        super(getWebsocket);
        mGetWebsocket = getWebsocket;
    }

    @Override
    protected void onInitialize(StatusScreen statusScreen) {
        mScreen = statusScreen;
        execute();
    }

    @Override
    protected void execute() {
        super.execute();
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
            Timber.d(websocket.toString());
        }
    }
}
