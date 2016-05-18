package com.nairbspace.octoandroid.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue
@AutoGson(autoValueClass = AutoValue_Websocket.class)
public abstract class Websocket {
    @Nullable @SerializedName("current") public abstract CurrentHistory current();
    @Nullable @SerializedName("history") public abstract CurrentHistory history();
    @Nullable @SerializedName("event") public abstract Event event();
    @Nullable @SerializedName("slicingProgress") public abstract SlicingProgress slicingProgress();

}
