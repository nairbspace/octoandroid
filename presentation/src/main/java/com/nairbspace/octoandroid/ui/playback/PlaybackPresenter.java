package com.nairbspace.octoandroid.ui.playback;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetPushNotificationSetting;
import com.nairbspace.octoandroid.domain.interactor.GetWebsocket;
import com.nairbspace.octoandroid.domain.interactor.SendJobCommand;
import com.nairbspace.octoandroid.domain.model.CurrentHistory;
import com.nairbspace.octoandroid.domain.model.SlicingProgress;
import com.nairbspace.octoandroid.domain.model.Websocket;
import com.nairbspace.octoandroid.mapper.SlicingProgressModelMapper;
import com.nairbspace.octoandroid.mapper.WebsocketModelMapper;
import com.nairbspace.octoandroid.model.JobCommandModel;
import com.nairbspace.octoandroid.model.SlicingProgressModel;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.UseCasePresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import timber.log.Timber;

public class PlaybackPresenter extends UseCasePresenter<PlaybackScreen> {

    private PlaybackScreen mScreen;

    private final GetWebsocket mGetWebsocket;
    private final WebsocketModelMapper mMapper;
    private final SendJobCommand mSendJobCommand;
    private final GetPushNotificationSetting mGetPushSetting;
    private final SlicingProgressModelMapper mProgressModelMapper;
    private final EventBus mEventBus;
    private boolean mIsPushOn;

    @Inject
    public PlaybackPresenter(GetWebsocket getWebsocket, WebsocketModelMapper mapper,
                             SendJobCommand sendJobCommand,
                             GetPushNotificationSetting getPushSetting,
                             SlicingProgressModelMapper progressModelMapper, EventBus eventBus) {
        super(getWebsocket);
        mGetWebsocket = getWebsocket;
        mMapper = mapper;
        mSendJobCommand = sendJobCommand;
        mGetPushSetting = getPushSetting;
        mProgressModelMapper = progressModelMapper;
        mEventBus = eventBus;
    }

    @Override
    protected void onInitialize(PlaybackScreen playbackScreen) {
        mScreen = playbackScreen;
    }

    @Override
    protected void onNetworkSwitched() {
        execute();
    }

    @Override
    protected void networkNowInactive() {
        super.networkNowInactive();
        mScreen.showNoFileLoadedScreen();
        mGetWebsocket.unsubscribe();
    }

    @Override
    protected void onResume() {
        execute();
        mScreen.setWebsocketServiceAndAlarm(false);
        mGetPushSetting.execute(new GetPushSubscriber());
    }

    @Override
    protected void onPause() {
        mGetWebsocket.unsubscribe(); // TODO might be best to have websocket on separate thread.
        if (mScreen.isPrinting()) mScreen.setWebsocketServiceAndAlarm(mIsPushOn);
    }

    @Override
    protected void execute() {
        mGetWebsocket.executeUnsubBg(new WebsocketSubscriber());
    }

    private final class WebsocketSubscriber extends DefaultSubscriber<Websocket> {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(e, null);
        }

        @Override
        public void onNext(Websocket websocket) {
            if (websocket == null) return;

            SlicingProgress progress = websocket.slicingProgress();
            if (progress != null) mProgressModelMapper.execute(new ProgressSubscriber(), progress);

            CurrentHistory current = websocket.current();
            if (current != null) mMapper.execute(new TransformSubscriber(), websocket);

            Timber.d(websocket.toString());
        }
    }

    private final class ProgressSubscriber extends DefaultSubscriber<SlicingProgressModel> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(SlicingProgressModel slicingProgressModel) {
            mEventBus.post(slicingProgressModel);
        }
    }

    private final class TransformSubscriber extends DefaultSubscriber<WebsocketModel> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(e, null);
        }

        @Override
        public void onNext(WebsocketModel websocketModel) {
            super.onNext(websocketModel);
            Timber.d(websocketModel.toString());
            renderScreen(websocketModel);
            mEventBus.post(websocketModel);
        }
    }

    public void renderScreen(WebsocketModel websocketModel) {
        boolean isFileLoaded = websocketModel.fileLoaded();
        boolean isPaused = websocketModel.paused();
        boolean isPrinting = websocketModel.printing();
        boolean isPausedOrPrinting = websocketModel.pausedOrPrinting();

        mScreen.updateSeekbar(websocketModel);

        if (!isPausedOrPrinting) {
            if (isFileLoaded) {
                mScreen.showFileLoadedScreen();
            } else {
                mScreen.showNoFileLoadedScreen();
            }
        }

        if (isPrinting) {
            mScreen.showPrintingScreen();
        }

        if (isPaused) {
            mScreen.showPausedScreen();
        }
    }

    public void onClickPressed(int viewId, WebsocketModel websocketModel) {
        if (viewId == mScreen.getPrintRestartId()) {
            if (websocketModel.paused()) {
                executeJobCommand(JobCommandModel.CommandType.RESTART);
            }
            if (!websocketModel.pausedOrPrinting() && websocketModel.fileLoaded()) {
                executeJobCommand(JobCommandModel.CommandType.START);
            }
        } else if (viewId == mScreen.getPausePlayId()) {
            if (websocketModel.printing()) {
                executeJobCommand(JobCommandModel.CommandType.PAUSE);
            }
            if (websocketModel.paused()) {
                executeJobCommand(JobCommandModel.CommandType.RESUME);
            }
        } else if (viewId == mScreen.getStopId()) {
            executeJobCommand(JobCommandModel.CommandType.CANCEL);
        }
    }

    private void executeJobCommand(JobCommandModel.CommandType type) {
        mSendJobCommand.execute(new SendJobSubscriber(), JobCommandModel.getCommand(type));
    }

    private final class SendJobSubscriber extends DefaultSubscriber {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }
    }

    private final class GetPushSubscriber extends DefaultSubscriber<Boolean> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Boolean aBoolean) {
            if (aBoolean != null) mIsPushOn = aBoolean;
        }
    }

    @Override
    protected void onDestroy(PlaybackScreen playbackScreen) {
        super.onDestroy(playbackScreen);
        mMapper.unsubscribe();
        mSendJobCommand.unsubscribe();
        mGetPushSetting.unsubscribe();
    }
}
