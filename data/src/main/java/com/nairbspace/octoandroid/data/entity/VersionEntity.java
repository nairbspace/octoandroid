package com.nairbspace.octoandroid.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue
@AutoGson(autoValueClass = AutoValue_VersionEntity.class)
public abstract class VersionEntity {
    @SerializedName("api") public abstract String api();
    @SerializedName("server") public abstract String server();
}
