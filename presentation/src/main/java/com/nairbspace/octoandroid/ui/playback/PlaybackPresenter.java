package com.nairbspace.octoandroid.ui.playback;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetWebsocket;
import com.nairbspace.octoandroid.domain.model.Websocket;
import com.nairbspace.octoandroid.mapper.WebsocketModelMapper;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

import timber.log.Timber;

public class PlaybackPresenter extends UseCasePresenter<PlaybackScreen> {

    private PlaybackScreen mScreen;
    private final GetWebsocket mGetWebsocket;
    private final WebsocketModelMapper mMapper;

    @Inject
    public PlaybackPresenter(GetWebsocket getWebsocket, WebsocketModelMapper mapper) {
        super(getWebsocket);
        mGetWebsocket = getWebsocket;
        mMapper = mapper;
    }

    @Override
    protected void onInitialize(PlaybackScreen playbackScreen) {
        mScreen = playbackScreen;
        execute();
    }

    @Override
    protected void onNetworkSwitched() {
        execute();
    }

    @Override
    protected void networkNowInactive() {
        super.networkNowInactive();
        mGetWebsocket.unsubscribe();
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
            mScreen.updateUi(websocketModel);
        }
    }
}
