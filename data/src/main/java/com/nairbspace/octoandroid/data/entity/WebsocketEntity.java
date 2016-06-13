package com.nairbspace.octoandroid.data.entity;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

@AutoValue
@AutoGson(autoValueClass = AutoValue_WebsocketEntity.class)
public abstract class WebsocketEntity {
    @Nullable @SerializedName("current") public abstract CurrentHistoryEntity current();
//    @Nullable @SerializedName("history") public abstract CurrentHistoryEntity history();
//    @Nullable @SerializedName("event") public abstract EventEntity event();
//    @Nullable @SerializedName("slicingProgress") public abstract SlicingProgressEntity slicingProgress();

}
