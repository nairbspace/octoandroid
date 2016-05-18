package com.nairbspace.octoandroid.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class WebsocketModel implements Parcelable {
    public abstract String state();
    public abstract String file();
    public abstract String approxTotalPrintTime();
    public abstract String printTimeLeft();
    public abstract String printTime();
    public abstract String printedBytes();
    public abstract String printedFileSize();
    public abstract int completionProgress();
    public static Builder builder() {
        return new AutoValue_WebsocketModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder state(String state);
        public abstract Builder file(String file);
        public abstract Builder approxTotalPrintTime(String approxTotalPrintTime);
        public abstract Builder printTimeLeft(String printTimeLeft);
        public abstract Builder printTime(String printTime);
        public abstract Builder printedBytes(String printedBytes);
        public abstract Builder printedFileSize(String printedFileSize);
        public abstract Builder completionProgress(int completionProgress);
        public abstract WebsocketModel build();
    }
}
