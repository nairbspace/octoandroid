package com.nairbspace.octoandroid.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.List;
import java.util.Map;

@AutoValue
public abstract class SlicingCommandModel implements Parcelable {
    public abstract int slicerPosition();
    public abstract int slicingProfilePosition();
    public abstract int printerProfilePosition();
    public abstract int afterSlicingPosition();
    public abstract String apiUrl();
    public abstract Map<String, SlicerModel> slicerMap();
    public abstract List<SpinnerModel> printerProfiles();
    public abstract List<String> afterSlicingList();

    public static Builder builder() {
        return new AutoValue_SlicingCommandModel.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder slicerPosition(int slicerPosition);
        public abstract Builder slicingProfilePosition(int slicingProfilePosition);
        public abstract Builder printerProfilePosition(int printerProfilePosition);
        public abstract Builder afterSlicingPosition(int afterSlicingPosition);
        public abstract Builder apiUrl(String apiUrl);
        public abstract Builder slicerMap(Map<String, SlicerModel> slicerMap);
        public abstract Builder printerProfiles(List<SpinnerModel> printerProfiles);
        public abstract Builder afterSlicingList(List<String> afterSlicingList);
        public abstract SlicingCommandModel build();
    }
}
