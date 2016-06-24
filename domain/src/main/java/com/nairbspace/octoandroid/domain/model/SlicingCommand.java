package com.nairbspace.octoandroid.domain.model;

import com.google.auto.value.AutoValue;

import java.util.List;
import java.util.Map;

@AutoValue
public abstract class SlicingCommand {
    public enum After {
        NOTHING, SELECT, PRINT,
    }
    public abstract int slicerPosition();
    public abstract int slicingProfilePosition();
    public abstract int printerProfilePosition();
    public abstract String apiUrl();
    public abstract Map<String, Slicer> slicerMap();
    public abstract List<String> printerProfilesIds();
    public abstract After after();

    public static Builder builder() {
        return new AutoValue_SlicingCommand.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder slicerPosition(int slicerPosition);
        public abstract Builder slicingProfilePosition(int slicingProfilePosition);
        public abstract Builder printerProfilePosition(int printerProfilePosition);
        public abstract Builder apiUrl(String apiUrl);
        public abstract Builder slicerMap(Map<String, Slicer> slicerMap);
        public abstract Builder printerProfilesIds(List<String> printerProfilesIds);
        public abstract Builder after(After after);
        public abstract SlicingCommand build();
    }
}
