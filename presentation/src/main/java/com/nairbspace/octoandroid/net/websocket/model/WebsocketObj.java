package com.nairbspace.octoandroid.net.websocket.model;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class WebsocketObj {

    @SerializedName("current") public abstract CurrentHistory current();
    @SerializedName("history") public abstract CurrentHistory history();
    @SerializedName("event") public abstract Event event();
    @SerializedName("slicingProgress") public abstract SlicingProgress slicingProgress();

}
