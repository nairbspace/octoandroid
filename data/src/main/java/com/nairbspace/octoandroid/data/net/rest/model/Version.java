package com.nairbspace.octoandroid.data.net.rest.model;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Version {
    @SerializedName("api") public abstract String api();
    @SerializedName("server") public abstract String server();
}
