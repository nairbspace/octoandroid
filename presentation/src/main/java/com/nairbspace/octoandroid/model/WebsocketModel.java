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
    public abstract boolean operational();
    public abstract boolean paused();
    public abstract boolean printing();
    public abstract boolean sdReady();
    public abstract boolean error();
    public abstract boolean ready();
    public abstract boolean closedOrError();
    public abstract boolean fileLoaded();
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
        public abstract Builder operational(boolean operational);
        public abstract Builder paused(boolean paused);
        public abstract Builder printing(boolean printing);
        public abstract Builder sdReady(boolean sdReady);
        public abstract Builder error(boolean error);
        public abstract Builder ready(boolean ready);
        public abstract Builder closedOrError(boolean closedOrError);
        public abstract Builder fileLoaded(boolean fileLoaded);
        public abstract WebsocketModel build();
    }
}
