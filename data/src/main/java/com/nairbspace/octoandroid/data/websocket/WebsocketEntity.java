package com.nairbspace.octoandroid.data.websocket;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

@AutoValue
@AutoGson(autoValueClass = AutoValue_WebsocketEntity.class)
public abstract class WebsocketEntity {
    @SerializedName("current") public abstract CurrentHistoryEntity current();
    @SerializedName("history") public abstract CurrentHistoryEntity history();
    @SerializedName("event") public abstract EventEntity event();
    @SerializedName("slicingProgress") public abstract SlicingProgressEntity slicingProgress();

}
