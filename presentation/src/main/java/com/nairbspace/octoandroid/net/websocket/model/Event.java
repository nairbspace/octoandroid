package com.nairbspace.octoandroid.net.websocket.model;

import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("type") private String type;
    @SerializedName("payload") private Object payload; // No doc on payload so spit out as object
}
