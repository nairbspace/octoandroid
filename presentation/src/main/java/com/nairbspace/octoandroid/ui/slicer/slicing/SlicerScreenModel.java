package com.nairbspace.octoandroid.ui.slicer.slicing;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.nairbspace.octoandroid.model.SlicerModel;
import com.nairbspace.octoandroid.model.SpinnerModel;

import java.util.List;
import java.util.Map;

@AutoValue
public abstract class SlicerScreenModel implements Parcelable {
    public abstract Map<String, SlicerModel> slicerModelMap();
    public abstract List<SpinnerModel> printerProfiles();

    public static SlicerScreenModel create(Map<String, SlicerModel> slicerModelMap,
                                           List<SpinnerModel> printerProfiles) {
        return new AutoValue_SlicerScreenModel(slicerModelMap, printerProfiles);
    }
}
