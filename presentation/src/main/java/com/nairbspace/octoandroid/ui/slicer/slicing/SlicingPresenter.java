package com.nairbspace.octoandroid.ui.slicer.slicing;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetConnectionDetails;
import com.nairbspace.octoandroid.domain.interactor.GetSlicers;
import com.nairbspace.octoandroid.domain.interactor.SendSliceCommand;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.domain.model.Slicer;
import com.nairbspace.octoandroid.domain.model.SlicingCommand;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.mapper.ConnectionMapper;
import com.nairbspace.octoandroid.mapper.SlicerModelMapper;
import com.nairbspace.octoandroid.model.ConnectModel;
import com.nairbspace.octoandroid.model.SlicerModel;
import com.nairbspace.octoandroid.model.SlicingCommandModel;
import com.nairbspace.octoandroid.model.SlicingProgressModel;
import com.nairbspace.octoandroid.ui.templates.UseCaseEventPresenter;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import timber.log.Timber;

public class SlicingPresenter extends UseCaseEventPresenter<SlicingScreen, SlicingProgressModel> {
    private static final int COMPLETE = 100;

    private Timer mTimer = new Timer();
    private static final int PROGRESS_DELAY = 4000; // in milliseconds

    private final GetSlicers mGetSlicers;
    private final SlicerModelMapper mSlicerModelMapper;
    private final GetConnectionDetails mConnectionDetails;
    private final ConnectionMapper mConnectionMapper;
    private final SlicerModelMapper.Command mCommandMapper;
    private final SendSliceCommand mSendSliceCommand;
    private SlicingScreen mScreen;

    @Inject
    public SlicingPresenter(GetSlicers getSlicers, SlicerModelMapper slicerModelMapper,
                            GetConnectionDetails connectionDetails,
                            ConnectionMapper connectionMapper,
                            SlicerModelMapper.Command commandMapper,
                            SendSliceCommand sendSliceCommand, EventBus eventBus) {
        super(eventBus, getSlicers, slicerModelMapper, connectionDetails,
                connectionMapper, commandMapper, sendSliceCommand);
        mGetSlicers = getSlicers;
        mSlicerModelMapper = slicerModelMapper;
        mConnectionDetails = connectionDetails;
        mConnectionMapper = connectionMapper;
        mCommandMapper = commandMapper;
        mSendSliceCommand = sendSliceCommand;
    }

    @Override
    protected void onInitialize(SlicingScreen slicingScreen) {
        mScreen = slicingScreen;
    }

    @Override
    protected void execute() {
        mGetSlicers.execute(new SlicerSubscriber());
        mConnectionDetails.execute(new ConnectionSubscriber());
    }

    @Override
    protected void onNetworkSwitched() {
        mScreen.enableSliceButton(true);
        execute();
    }

    @Override
    protected void networkNowInactive() {
        mScreen.enableSliceButton(false);
    }

    @Override
    protected void onEvent(SlicingProgressModel progressModel) {
        if (progressModel.progress() == COMPLETE) {
            mTimer.cancel();
            mScreen.showProgress(false);
            mScreen.showSliceCompleteAndUpdateFiles();
        } else {
            mScreen.updateProgress(progressModel);
        }

        // Websocket might miss 100% mark, if so manually refresh screen after
        // certain amount of time with no update.
        if (progressModel.progress() > 0) {
            mTimer.cancel();
            startTimer();
        }
    }

    private void startTimer() {
        mTimer = new Timer();
        mTimer.schedule(new ProgressTask(), PROGRESS_DELAY);
    }

    private final class ProgressTask extends TimerTask {

        @Override
        public void run() {
            execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mScreen.isSlicingInProgress()) startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTimer.cancel();
    }

    private final class SlicerSubscriber extends DefaultSubscriber<Map<String, Slicer>> {

        @Override
        public void onError(Throwable e) {
            handleError(e);
        }

        @Override
        public void onNext(Map<String, Slicer> slicerMap) {
            mSlicerModelMapper.execute(new SlicerModelSubscriber(), slicerMap);
        }
    }

    private final class SlicerModelSubscriber extends DefaultSubscriber<List<SlicerModel>> {

        @Override
        public void onError(Throwable e) {
            handleError(e);
        }

        @Override
        public void onNext(List<SlicerModel> models) {
            mScreen.updateSlicer(models);
        }
    }

    private final class ConnectionSubscriber extends DefaultSubscriber<Connection> {
        @Override
        public void onError(Throwable e) {
            handleError(e);
        }

        @Override
        public void onNext(Connection connection) {
            mConnectionMapper.execute(new ConnectMapperSubscriber(), connection);
        }
    }

    private final class ConnectMapperSubscriber extends DefaultSubscriber<ConnectModel> {
        @Override
        public void onError(Throwable e) {
            handleError(e);
        }

        @Override
        public void onNext(ConnectModel connectModel) {
            mScreen.updatePrinterProfile(connectModel.printerProfiles());
        }
    }

    public String getFileName(String apiUrl) {
        try {
            apiUrl = URLDecoder.decode(apiUrl, "UTF-8"); // StandardCharsets.UTF_8.name() > SDK 19, lame.
            File file = new File(apiUrl);
            return changeFileExtToGco(file.getName());
        } catch (NullPointerException | UnsupportedEncodingException e) {
            return "";
        }
    }

    private String changeFileExtToGco(String fileName) {
        try {
            String fileNameWithOutExt = fileName.substring(0, fileName.lastIndexOf("."));
            return fileNameWithOutExt + mScreen.getDotGco();
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    protected void onSliceButtonClicked(SlicingCommandModel.Builder builder) {
        try {
            SlicingCommandModel model = builder.build();
            if (arePositionsInvalid(model)) mScreen.toastSlicingParametersMissing();
            else mCommandMapper.execute(new CommandMapper(), model);
        } catch (IllegalStateException e) {
            mScreen.toastSlicingParametersMissing();
        }
    }

    private boolean arePositionsInvalid(SlicingCommandModel model) {
        final int invalidPosition = mScreen.getInvalidPosition();
        final int slicerPosition = model.slicerPosition();
        final int slicingProfilePosition = model.slicingProfilePosition();
        final int printerProfilePosition = model.printerProfilePosition();
        final int afterSlicingPosition = model.afterSlicingPosition();
        return (slicerPosition == invalidPosition || slicingProfilePosition == invalidPosition ||
                printerProfilePosition == invalidPosition || afterSlicingPosition == invalidPosition);
    }

    private final class CommandMapper extends DefaultSubscriber<SlicingCommand> {
        @Override
        public void onError(Throwable e) {
            handleError(e);
        }

        @Override
        public void onNext(SlicingCommand slicingCommand) {
            mSendSliceCommand.execute(new SliceCommandSubscriber(), slicingCommand);
        }
    }

    private final class SliceCommandSubscriber extends DefaultSubscriber {
        @Override
        public void onError(Throwable e) {
            handleError(e);
        }

        @Override
        public void onNext(Object o) {
            if (o == null) return;
            // TODO should show response from octoprint + data from slice command as inital progress
            mScreen.updateProgress(SlicingProgressModel.initial());
        }
    }

    private void handleError(Throwable t) {
        // TODO possibly display better error message
        Timber.e(t, null);
        String message = ErrorMessageFactory.create(mScreen.context(), t);
        mScreen.setRefresh(false);
        mScreen.toastMessage(message);
    }

    @Override
    protected void onDestroy(SlicingScreen slicingScreen) {
        super.onDestroy(slicingScreen);
        mTimer.cancel();
    }
}
