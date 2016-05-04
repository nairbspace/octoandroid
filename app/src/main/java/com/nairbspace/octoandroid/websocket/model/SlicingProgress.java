package com.nairbspace.octoandroid.websocket.model;

import com.google.gson.annotations.SerializedName;

public class SlicingProgress {
    @SerializedName("slicer") private String slicer;
    @SerializedName("source_location") private String sourceLocation;
    @SerializedName("source_patch") private String sourcePath;
    @SerializedName("dest_location") private String destLocation;
    @SerializedName("dest_path") private String destPath;
    @SerializedName("progress") private Float progress;
}
