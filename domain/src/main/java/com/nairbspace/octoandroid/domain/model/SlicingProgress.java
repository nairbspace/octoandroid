package com.nairbspace.octoandroid.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
//@AutoGson(autoValueClass = AutoValue_SlicingProgress.class)
public abstract class SlicingProgress {
    @Nullable @SerializedName("slicer") public abstract String slicer();
    @Nullable @SerializedName("source_location") public abstract String sourceLocation();
    @Nullable @SerializedName("source_path") public abstract String sourcePath();
    @Nullable @SerializedName("dest_location") public abstract String destLocation();
    @Nullable @SerializedName("dest_path") public abstract String destPath();
    @Nullable @SerializedName("progress") public abstract Double progress();

    public static TypeAdapter<SlicingProgress> typeAdapter(Gson gson) {
        return new AutoValue_SlicingProgress.GsonTypeAdapter(gson);
    }
}
