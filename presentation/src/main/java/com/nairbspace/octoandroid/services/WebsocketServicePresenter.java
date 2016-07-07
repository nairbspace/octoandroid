package com.nairbspace.octoandroid.services;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetPushNotificationSetting;
import com.nairbspace.octoandroid.domain.interactor.GetStickyNotificationSetting;
import com.nairbspace.octoandroid.domain.interactor.GetWebsocket;
import com.nairbspace.octoandroid.domain.model.Websocket;
import com.nairbspace.octoandroid.mapper.WebsocketModelMapper;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.services.WebsocketService.FinishType;
import com.nairbspace.octoandroid.ui.templates.UseCasePresenter;

import javax.inject.Inject;

import timber.log.Timber;

public class WebsocketServicePresenter extends UseCasePresenter<WebsocketServiceListener> {
    private static final int COMPLETE = 100;

    private final GetWebsocket mGetWebsocket;
    private final WebsocketModelMapper mWebsocketModelMapper;
    private final GetPushNotificationSetting mGetPushSetting;
    private final GetStickyNotificationSetting mGetStickySetting;

    private WebsocketServiceListener mListener;
    private boolean mStickyOn;

    @Inject
    public WebsocketServicePresenter(GetWebsocket getWebsocket,
                                     WebsocketModelMapper websocketModelMapper,
                                     GetPushNotificationSetting getPushSetting,
                                     GetStickyNotificationSetting getStickySetting) {
        super(getWebsocket, websocketModelMapper, getPushSetting, getStickySetting);
        mGetWebsocket = getWebsocket;
        mWebsocketModelMapper = websocketModelMapper;
        mGetPushSetting = getPushSetting;
        mGetStickySetting = getStickySetting;
    }

    @Override
    protected void onInitialize(WebsocketServiceListener listener) {
        Timber.d("onCreate");
        mListener = listener;
    }

    protected void onStartCommand() {
        Timber.d("onStartCommand");
        mGetWebsocket.executeAllBg(new WebsocketSubscriber());
        mGetStickySetting.executeAllBg(new StickySubscriber());
    }

    private final class StickySubscriber extends DefaultSubscriber<Boolean> {
        @Override
        public void onError(Throwable e) {
            subscriberError(e);
        }

        @Override
        public void onNext(Boolean aBoolean) {
            if (aBoolean != null) mStickyOn = aBoolean;
        }
    }

    private final class WebsocketSubscriber extends DefaultSubscriber<Websocket> {
        @Override
        public void onError(Throwable e) {
            subscriberError(e);
        }

        @Override
        public void onNext(Websocket websocket) {
            mWebsocketModelMapper.executeAllBg(new MapperSubscriber(), websocket);
        }
    }

    private final class MapperSubscriber extends DefaultSubscriber<WebsocketModel> {
        @Override
        public void onError(Throwable e) {
            subscriberError(e);
        }

        @Override
        public void onNext(WebsocketModel websocketModel) {
            Timber.d(websocketModel.toString());
            mListener.checkApplicationStatus();
            if (mStickyOn) mListener.showSticky(websocketModel);
            checkPrintStatus(websocketModel);
        }
    }

    /**
     * If print is complete show notification and stop service.
     * If closed or error show error notification and stop service
     * @param model {@link WebsocketModel}
     */
    private void checkPrintStatus(WebsocketModel model) {
        if (model.completionProgress() == COMPLETE) {
            mGetPushSetting.executeAllBg(new PushSubscriber(model.file()));
        } else if (model.closedOrError() || model.error()) {
            mListener.showFinishedAndDestroy(FinishType.ERROR, null);
        } else if (!model.pausedOrPrinting()) {
            // TODO-low would be nice in feature to pause/resume print from notification!
            mListener.showFinishedAndDestroy(FinishType.NONE, null);
        }
    }

    private final class PushSubscriber extends DefaultSubscriber<Boolean> {

        private final String mFileName;

        private PushSubscriber(String fileName) {
            this.mFileName = fileName;
        }

        @Override
        public void onError(Throwable e) {
            subscriberError(e);
        }

        @Override
        public void onNext(Boolean aBoolean) {
            if (aBoolean != null) mListener.showFinishedAndDestroy(FinishType.REGULAR, mFileName);
        }
    }

    private void subscriberError(Throwable t) {
        Timber.e(t, null);
        mListener.showFinishedAndDestroy(FinishType.ERROR, null);
    }
}
