package com.nairbspace.octoandroid.domain.model;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue
@AutoGson(autoValueClass = AutoValue_Websocket.class)
public abstract class Websocket {
    @SerializedName("current") public abstract CurrentHistory current();
    @SerializedName("history") public abstract CurrentHistory history();
    @SerializedName("event") public abstract Event event();
    @SerializedName("slicingProgress") public abstract SlicingProgress slicingProgress();

}
