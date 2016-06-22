package com.nairbspace.octoandroid.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

@AutoValue
@AutoGson(autoValueClass = AutoValue_SlicingCommandEntity.class)
public abstract class SlicingCommandEntity {
    private static final String SLICE_COMMAND = "slice";

    //    public abstract String gcode(); // If not set will just use default name
    @SerializedName("command") public abstract String command();
    @SerializedName("profile") public abstract String slicerProfile();
    @SerializedName("slicer") public abstract String slicer();
    @SerializedName("printerProfile") public abstract String printerProfile();
    @SerializedName("print") public abstract boolean print();
    @SerializedName("select") public abstract boolean select();

    public static Builder builder() {
        return new AutoValue_SlicingCommandEntity.Builder().command(SLICE_COMMAND);
    }


    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder command(String command);
        public abstract Builder slicer(String slicer);
        public abstract Builder printerProfile(String printerProfile);
        public abstract Builder slicerProfile(String slicerProfile);
        public abstract Builder print(boolean print);
        public abstract Builder select(boolean select);

        public abstract SlicingCommandEntity build();
    }
}
