package com.nairbspace.octoandroid.ui.slicer.slicing;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.nairbspace.octoandroid.model.SlicerModel;

import java.util.HashMap;
import java.util.Map;

@AutoValue
public abstract class SlicerScreenModel implements Parcelable {
    public abstract Map<String, SlicerModel> slicerModelMap();
    public abstract HashMap<String, String> printerProfileNamesMap();

    public static SlicerScreenModel create(Map<String, SlicerModel> slicerModelMap,
                                           HashMap<String, String> printerProfileNamesMap) {
        return new AutoValue_SlicerScreenModel(slicerModelMap, printerProfileNamesMap);
    }
}
