package com.nairbspace.octoandroid.data.websocket;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

@AutoValue
@AutoGson(autoValueClass = AutoValue_EventEntity.class)
public abstract class EventEntity {
    @SerializedName("type") public abstract String type();
    @SerializedName("payload") public abstract Object payload(); // No doc on payload so spit out as object
}
