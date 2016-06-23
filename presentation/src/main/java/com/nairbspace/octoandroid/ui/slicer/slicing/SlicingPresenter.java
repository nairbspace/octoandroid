package com.nairbspace.octoandroid.ui.slicer.slicing;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetConnectionDetails;
import com.nairbspace.octoandroid.domain.interactor.GetSlicers;
import com.nairbspace.octoandroid.domain.interactor.SendSliceCommand;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.domain.model.Slicer;
import com.nairbspace.octoandroid.domain.model.SlicingCommand;
import com.nairbspace.octoandroid.mapper.ConnectionMapper;
import com.nairbspace.octoandroid.mapper.SlicerModelMapper;
import com.nairbspace.octoandroid.model.ConnectModel;
import com.nairbspace.octoandroid.model.SlicerModel;
import com.nairbspace.octoandroid.model.SlicingCommandModel;
import com.nairbspace.octoandroid.model.SlicingProgressModel;
import com.nairbspace.octoandroid.ui.templates.UseCaseEventPresenter;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class SlicingPresenter extends UseCaseEventPresenter<SlicingScreen, SlicingProgressModel> {
    private static final int COMPLETE = 100;

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
        super(getSlicers, eventBus);
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
        execute();
    }

    @Override
    protected void execute() {
        mGetSlicers.execute(new SlicerSubscriber());
        mConnectionDetails.execute(new ConnectionSubscriber());
    }

    @Override
    protected void onEvent(SlicingProgressModel progressModel) {
        if (progressModel.progress() == COMPLETE) {
            mScreen.showProgress(false);
            mScreen.showSliceCompleteAndUpdateFiles();
        } else {
            mScreen.updateProgress(progressModel);
        }
    }

    private final class SlicerSubscriber extends DefaultSubscriber<Map<String, Slicer>> {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Map<String, Slicer> slicerMap) {
            mSlicerModelMapper.execute(new SlicerModelSubscriber(), slicerMap);
        }
    }

    private final class SlicerModelSubscriber extends DefaultSubscriber<Map<String, SlicerModel>> {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Map<String, SlicerModel> map) {
            mScreen.updateSlicer(map, getSlicerNames(map));
        }
    }

    private final class ConnectionSubscriber extends DefaultSubscriber<Connection> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Connection connection) {
            mConnectionMapper.execute(new ConnectMapperSubscriber(), connection);
        }
    }

    private final class ConnectMapperSubscriber extends DefaultSubscriber<ConnectModel> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(ConnectModel connectModel) {
            HashMap<String, String> map = connectModel.printerProfiles();
            mScreen.updatePrinterProfile(map, getPrinterProfileNames(map));
        }
    }

    public String getFileName(String apiUrl) {
        try {
            File file = new File(apiUrl);
            return changeFileExtToGco(file.getName());
        } catch (NullPointerException e) {
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

    public List<String> getPrinterProfileNames(HashMap<String, String> map) {
        List<String> names = new ArrayList<>();
        names.addAll(map.values());
        return names;
    }

    public List<String> getProfileNames(Map<String, SlicerModel> modelMap, int position) {
        SlicerModel slicerModel = getSlicerModel(modelMap, position);
        List<SlicerModel.Profile> profiles = getProfiles(slicerModel);
        if (profiles == null) return null;
        return getProfileNames(profiles);
    }

    private List<SlicerModel.Profile> getProfiles(SlicerModel slicerModel) {
        Map<String, SlicerModel.Profile> profileMap = slicerModel.profiles();
        if (profileMap == null) return null;
        List<SlicerModel.Profile> profiles = new ArrayList<>();
        profiles.addAll(profileMap.values());
        return profiles;
    }

    private List<SlicerModel> getSlicerModels(Map<String, SlicerModel> map) {
        List<SlicerModel> slicerModels = new ArrayList<>();
        slicerModels.addAll(map.values());
        return slicerModels;
    }

    private SlicerModel getSlicerModel(Map<String, SlicerModel> map, int position) {
        List<SlicerModel> slicerModels = getSlicerModels(map);
        return getSlicerModel(slicerModels, position);
    }

    private SlicerModel getSlicerModel(List<SlicerModel> models, int position) {
        try {
            return models.get(position);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public List<String> getSlicerNames(Map<String, SlicerModel> modelMap) {
        List<SlicerModel> models = new ArrayList<>();
        models.addAll(modelMap.values());
        return getSlicerNames(models);
    }

    private List<String> getSlicerNames(List<SlicerModel> models) {
        List<String> slicerNames = new ArrayList<>();
        for (SlicerModel slicerModel : models) {
            String name = slicerModel.displayName();
            if (name != null) slicerNames.add(name);
        }
        return slicerNames;
    }
    private List<String> getProfileNames(List<SlicerModel.Profile> profiles) {
        List<String> profileNames = new ArrayList<>();
        for (SlicerModel.Profile profile : profiles) {
            String name = profile.displayName();
            if (name != null) profileNames.add(name);
        }
        return profileNames;
    }

    protected void onSliceButtonClicked(SlicingCommandModel model) {
        mCommandMapper.execute(new CommandMapper(), model);
    }

    private final class CommandMapper extends DefaultSubscriber<SlicingCommand> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(SlicingCommand slicingCommand) {
            mSendSliceCommand.execute(new SliceCommandSubscriber(), slicingCommand);
        }
    }

    private final class SliceCommandSubscriber extends DefaultSubscriber {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Object o) {
            if (o == null) return;
            // TODO should show response from octoprint + data from slice command as inital progress
            mScreen.updateProgress(SlicingProgressModel.initial());
        }
    }

    @Override
    protected void onDestroy(SlicingScreen slicingScreen) {
        super.onDestroy(slicingScreen);
        mSlicerModelMapper.unsubscribe();
        mConnectionDetails.unsubscribe();
        mConnectionMapper.unsubscribe();
        mCommandMapper.unsubscribe();
        mSendSliceCommand.unsubscribe();
    }
}
