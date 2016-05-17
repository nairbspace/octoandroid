package com.nairbspace.octoandroid.data.websocket;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

@AutoValue
@AutoGson(autoValueClass = AutoValue_SlicingProgressEntity.class)
public abstract class SlicingProgressEntity {
    @SerializedName("slicer") public abstract String slicer();
    @SerializedName("source_location") public abstract String sourceLocation();
    @SerializedName("source_patch") public abstract String sourcePath();
    @SerializedName("dest_location") public abstract String destLocation();
    @SerializedName("dest_path") public abstract String destPath();
    @SerializedName("progress") public abstract Float progress();
}
