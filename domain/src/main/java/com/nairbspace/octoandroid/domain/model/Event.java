package com.nairbspace.octoandroid.domain.model;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue
@AutoGson(autoValueClass = AutoValue_Event.class)
public abstract class Event {
    @SerializedName("type") public abstract String type();
    @SerializedName("payload") public abstract Object payload(); // No doc on payload so spit out as object
}
