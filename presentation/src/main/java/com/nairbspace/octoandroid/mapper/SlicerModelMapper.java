package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Slicer;
import com.nairbspace.octoandroid.domain.model.SlicingCommand;
import com.nairbspace.octoandroid.model.SlicerModel;
import com.nairbspace.octoandroid.model.SlicingCommandModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class SlicerModelMapper extends MapperUseCase<Map<String, Slicer>, List<SlicerModel>> {

    @Inject
    public SlicerModelMapper(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable<List<SlicerModel>> buildUseCaseObservableInput(final Map<String, Slicer> slicerMap) {
        return Observable.create(new Observable.OnSubscribe<List<SlicerModel>>() {
            @Override
            public void call(Subscriber<? super List<SlicerModel>> subscriber) {
                try {
                    subscriber.onNext(maptoSlicerModel(slicerMap));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private List<SlicerModel> maptoSlicerModel(Map<String, Slicer> slicerMap) {
        List<String> keys = new ArrayList<>();
        keys.addAll(slicerMap.keySet());
        List<SlicerModel> slicerModels = new ArrayList<>();
        for (String key : keys) {
            Slicer slicer = slicerMap.get(key);
            List<SlicerModel.Profile> profiles = mapToProfiles(slicer.profiles());
            Boolean getIsDefault = slicer.isDefault();
            boolean isDefault = false;
            if (getIsDefault != null) isDefault = getIsDefault;
            SlicerModel slicerModel = new SlicerModel(slicer.key(), slicer.displayName(), isDefault, profiles);
            slicerModels.add(slicerModel);
        }

        return slicerModels;
    }

    private List<SlicerModel.Profile> mapToProfiles(Map<String, Slicer.Profile> profileMap) {
        List<String> keys = new ArrayList<>();
        keys.addAll(profileMap.keySet());
        List<SlicerModel.Profile> profiles = new ArrayList<>();
        for (String key : keys) {
            Slicer.Profile profile = profileMap.get(key);
            Boolean getIsDefault = profile.isDefault();
            boolean isDefault = false;
            if (getIsDefault != null) isDefault = getIsDefault;
            SlicerModel.Profile profileModel = new SlicerModel.Profile(profile.key(),
                    profile.displayName(), isDefault, profile.resource());
            profiles.add(profileModel);
        }

        return profiles;
    }

    public static class Command extends MapperUseCase<SlicingCommandModel, SlicingCommand> {

        @Inject
        public Command(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
            super(threadExecutor, postExecutionThread);
        }

        @Override
        protected Observable<SlicingCommand> buildUseCaseObservableInput(final SlicingCommandModel model) {
            return Observable.create(new Observable.OnSubscribe<SlicingCommand>() {
                @Override
                public void call(Subscriber<? super SlicingCommand> subscriber) {
                    try {
                        subscriber.onNext(getSlicingCommand(model));
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
            });
        }

        private SlicingCommand getSlicingCommand(SlicingCommandModel model) {
            int afterPosition = model.afterSlicingPosition();
            SlicingCommand.After after;
            if (afterPosition == 1) {
                after = SlicingCommand.After.SELECT;
            } else if (afterPosition == 2) {
                after = SlicingCommand.After.PRINT;
            } else {
                after = SlicingCommand.After.NOTHING;
            }

            List<String> printerProfileIds = new ArrayList<>();
            for (int i = 0; i < model.printerProfiles().size(); i++) {
                printerProfileIds.add(model.printerProfiles().get(i).id());
            }

            int spinnerSelectedPrinterProfile = model.printerProfilePosition();
            String id = model.printerProfiles().get(spinnerSelectedPrinterProfile).id();
            int selectedPrinterProfileId = 0;
            for (int i = 0; i < printerProfileIds.size(); i++) {
                if (printerProfileIds.get(i).equals(id)) {
                    selectedPrinterProfileId = i;
                }
            }

            Map<String, Slicer> slicerMap = mapToSlicerMap(model.slicerModels());

            return SlicingCommand.builder()
                    .slicerPosition(model.slicerPosition())
                    .slicingProfilePosition(model.slicingProfilePosition())
                    .printerProfilePosition(selectedPrinterProfileId)
                    .apiUrl(model.apiUrl())
                    .slicerMap(slicerMap)
                    .printerProfilesIds(printerProfileIds)
                    .after(after)
                    .build();
        }

        private Map<String, Slicer> mapToSlicerMap(List<SlicerModel> models) {
            Map<String, Slicer> map = new HashMap<>();
            for (SlicerModel model : models) {
                Map<String, Slicer.Profile> profileMap = mapToSlicerProfileMap(model.profiles());
                Slicer slicer = Slicer.create(model.key(), model.displayName(), model.isDefault(), profileMap);
                map.put(model.key(), slicer);
            }

            return map;
        }

        private Map<String, Slicer.Profile> mapToSlicerProfileMap(List<SlicerModel.Profile> profiles) {
            Map<String, Slicer.Profile> map = new HashMap<>();
            for (SlicerModel.Profile profileModel : profiles) {
                Slicer.Profile profile = Slicer.Profile.create(profileModel.key(), profileModel.displayName(),
                        profileModel.isDefault(), profileModel.resource());
                map.put(profileModel.key(), profile);
            }

            return map;
        }
    }
}
