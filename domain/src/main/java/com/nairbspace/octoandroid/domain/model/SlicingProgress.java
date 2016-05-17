package com.nairbspace.octoandroid.domain.model;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue
@AutoGson(autoValueClass = AutoValue_SlicingProgress.class)
public abstract class SlicingProgress {
    @SerializedName("slicer") public abstract String slicer();
    @SerializedName("source_location") public abstract String sourceLocation();
    @SerializedName("source_patch") public abstract String sourcePath();
    @SerializedName("dest_location") public abstract String destLocation();
    @SerializedName("dest_path") public abstract String destPath();
    @SerializedName("progress") public abstract Float progress();
}
