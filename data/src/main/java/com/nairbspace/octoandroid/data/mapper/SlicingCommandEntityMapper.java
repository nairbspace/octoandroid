package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.entity.SlicingCommandEntity;
import com.nairbspace.octoandroid.data.exception.EntityMapperException;
import com.nairbspace.octoandroid.domain.model.Slicer;
import com.nairbspace.octoandroid.domain.model.SlicingCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

public class SlicingCommandEntityMapper {

    public static Observable.OnSubscribe<SlicingCommandEntity> mapToEntity(final SlicingCommand slicingCommand) {
        return new Observable.OnSubscribe<SlicingCommandEntity>() {
            @Override
            public void call(Subscriber<? super SlicingCommandEntity> subscriber) {
                try {
                    subscriber.onNext(getEntity(slicingCommand));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new EntityMapperException(e));
                }
            }
        };
    }

    public static SlicingCommandEntity getEntity(SlicingCommand slicingCommand) {
        int slicerPosition = slicingCommand.slicerPosition();
        int slicerProfilePosition = slicingCommand.slicingProfilePosition();
        int printerProfilePosition = slicingCommand.printerProfilePosition();

        List<Slicer> slicerList = getSlicers(slicingCommand);
        Slicer slicer = slicerList.get(slicerPosition);
        String slicerKey = slicer.key();
        List<Slicer.Profile> profileList = getProfiles(slicer);
        String slicerProfileKey = getSlicerProfileKey(profileList, slicerProfilePosition);
        String printerProfileKey = getPrinterProfileKey(slicingCommand.printerProfileMap(), printerProfilePosition);
        boolean select = false;
        boolean print = false;

        switch (slicingCommand.after()) {
            case SELECT:
                select = true;
                break;
            case PRINT:
                print = true;
                break;
        }

        return SlicingCommandEntity.builder()
                .slicer(slicerKey)
                .slicerProfile(slicerProfileKey)
                .printerProfile(printerProfileKey)
                .select(select)
                .print(print)
                .build();
    }

    public static String getPrinterProfileKey(HashMap<String, String> map, int position) {
        List<String> printerProfilesKeysList = new ArrayList<>();
        printerProfilesKeysList.addAll(map.keySet());
        return printerProfilesKeysList.get(position);
    }

    public static String getSlicerProfileKey(List<Slicer.Profile> profileList, int position) {
        return profileList.get(position).key();
    }

    public static List<Slicer> getSlicers(SlicingCommand slicingCommand) {
        List<Slicer> slicerList = new ArrayList<>();
        slicerList.addAll(slicingCommand.slicerMap().values());
        return slicerList;
    }

    private static List<Slicer.Profile> getProfiles(Slicer slicer) {
        Map<String, Slicer.Profile> profileMap = slicer.profiles();
        if (profileMap == null) return null;
        List<Slicer.Profile> profiles = new ArrayList<>();
        profiles.addAll(profileMap.values());
        return profiles;
    }
}
