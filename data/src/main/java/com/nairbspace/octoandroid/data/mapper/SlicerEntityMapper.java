package com.nairbspace.octoandroid.data.mapper;

import com.google.gson.reflect.TypeToken;
import com.nairbspace.octoandroid.data.entity.SlicerEntity;
import com.nairbspace.octoandroid.domain.model.Slicer;

import java.lang.reflect.Type;
import java.util.Map;

import rx.functions.Func1;

public class SlicerEntityMapper {

    public static Func1<Map<String, SlicerEntity>, Map<String, Slicer>> mapToSlicer(final EntitySerializer serializer) {
        return new Func1<Map<String, SlicerEntity>, Map<String, Slicer>>() {
            @Override
            public Map<String, Slicer> call(Map<String, SlicerEntity> slicerEntityMap) {
                Type type = new TypeToken<Map<String, Slicer>>(){}.getType();
                return serializer.transform(slicerEntityMap, type);
            }
        };
    }
}
