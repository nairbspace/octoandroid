package com.nairbspace.octoandroid.domain.pojo;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue
@AutoGson(autoValueClass = AutoValue_Version.class)
public abstract class Version {
    @SerializedName("api") public abstract String api();
    @SerializedName("server") public abstract String server();
}
