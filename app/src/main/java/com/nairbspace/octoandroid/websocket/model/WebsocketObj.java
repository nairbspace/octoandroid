package com.nairbspace.octoandroid.websocket.model;

import com.google.gson.annotations.SerializedName;

public class WebsocketObj {

    @SerializedName("current") private Current current;
    @SerializedName("history") private History history;
    @SerializedName("event") private Event event;
    @SerializedName("slicingProgress") private SlicingProgress slicingProgress;
}
