package com.nairbspace.octoandroid.services;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetPushNotificationSetting;
import com.nairbspace.octoandroid.domain.interactor.GetStickyNotificationSetting;
import com.nairbspace.octoandroid.domain.interactor.GetWebsocket;
import com.nairbspace.octoandroid.domain.model.Websocket;
import com.nairbspace.octoandroid.mapper.WebsocketModelMapper;
import com.nairbspace.octoandroid.model.WebsocketModel;

import javax.inject.Inject;

import timber.log.Timber;

public class WebsocketServiceHelper {
    private static final int COMPLETE = 100;

    private final GetWebsocket mGetWebsocket;
    private final WebsocketModelMapper mWebsocketModelMapper;
    private final GetPushNotificationSetting mGetPushSetting;
    private final GetStickyNotificationSetting mGetStickySetting;

    private Listener mListener;
    private boolean mWasPreviouslyPrinting;
    private boolean mStickyOn;

    @Inject
    public WebsocketServiceHelper(GetWebsocket getWebsocket,
                                  WebsocketModelMapper websocketModelMapper,
                                  GetPushNotificationSetting getPushSetting,
                                  GetStickyNotificationSetting getStickySetting) {
        mGetWebsocket = getWebsocket;
        mWebsocketModelMapper = websocketModelMapper;
        mGetPushSetting = getPushSetting;
        mGetStickySetting = getStickySetting;
    }

    protected void onCreate(Listener listener) {
        Timber.d("onCreate");
        mListener = listener;
    }

    protected void onStartCommand() {
        Timber.d("Websocket service started");
        mGetWebsocket.execute(new WebsocketSubscriber());
        mGetStickySetting.execute(new StickySubscriber());
    }

    private final class StickySubscriber extends DefaultSubscriber<Boolean> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Boolean aBoolean) {
            if (aBoolean != null) mStickyOn = aBoolean;
        }
    }

    private final class WebsocketSubscriber extends DefaultSubscriber<Websocket> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Websocket websocket) {
            mWebsocketModelMapper.execute(new MapperSubscriber(), websocket);
        }
    }

    private final class MapperSubscriber extends DefaultSubscriber<WebsocketModel> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
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
     * Checks to see if last state was previously printing. If it was then
     * it checks if current status is complete which finishes this service.
     * Otherwise it stores current state as previous.
     * @param model {@link WebsocketModel}
     */
    private void checkPrintStatus(WebsocketModel model) {
        if (mWasPreviouslyPrinting && model.completionProgress() == COMPLETE) {
            mGetPushSetting.execute(new PushSubscriber(model.file()));
        } else {
            mWasPreviouslyPrinting = model.printing();
        }
    }

    private final class PushSubscriber extends DefaultSubscriber<Boolean> {

        private final String mFileName;

        private PushSubscriber(String fileName) {
            this.mFileName = fileName;
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Boolean aBoolean) {
            if (aBoolean != null) mListener.showFinishedAndDestroy(mFileName, aBoolean);
        }
    }

    protected void onDestroy() {
        Timber.d("onDestroy");
        mGetWebsocket.unsubscribe();
        mWebsocketModelMapper.unsubscribe();
        mGetPushSetting.unsubscribe();
        mGetStickySetting.unsubscribe();
        mListener = null;
    }

    public interface Listener {
        void turnOffAlarmAndStopService();
        void showSticky(WebsocketModel model);
        void showFinishedAndDestroy(String fileName, boolean showFinish);
        void checkApplicationStatus();
    }
}
