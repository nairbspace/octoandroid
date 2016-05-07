package com.nairbspace.octoandroid.net.websocket.model;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Event {
    @SerializedName("type") public abstract String type();
    @SerializedName("payload") public abstract Object payload(); // No doc on payload so spit out as object
}
