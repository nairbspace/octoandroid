package com.nairbspace.octoandroid.ui.slicer.slicing;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.nairbspace.octoandroid.model.SlicerModel;
import com.nairbspace.octoandroid.model.SpinnerModel;

import java.util.List;

@AutoValue
public abstract class SlicerScreenModel implements Parcelable {
    public abstract List<SlicerModel> slicerModels();
    public abstract List<SpinnerModel> printerProfiles();

    public static SlicerScreenModel create(List<SlicerModel> slicerModels,
                                           List<SpinnerModel> printerProfiles) {
        return new AutoValue_SlicerScreenModel(slicerModels, printerProfiles);
    }
}
