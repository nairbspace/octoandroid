package com.nairbspace.octoandroid.net.rest.model;

import com.google.gson.annotations.SerializedName;

public class Version {

    @SerializedName("api") private String api;
    @SerializedName("server") private String server;

    public String getApi() {
        return api;
    }

    public String getServer() {
        return server;
    }
}
