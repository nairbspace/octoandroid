package com.nairbspace.octoandroid.websocket.model;

import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("type") private String type;
    @SerializedName("payload") private String payload; // TODO this spits out an object, but just leave as JSON string for now
}
