package com.nairbspace.octoandroid.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class WebsocketModel implements Parcelable {
    public abstract List<String> logs();
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
    public abstract boolean pausedOrPrinting();
    public abstract boolean sdReady();
    public abstract boolean error();
    public abstract boolean ready();
    public abstract boolean closedOrError();
    public abstract boolean fileLoaded();

    public abstract String tempTime();
    public abstract float actualTempBed();
    public abstract float targetTempBed();
    public abstract float actualTempTool0();
    public abstract float targetTempTool0();
    public abstract float actualTempTool1();
    public abstract float targetTempTool1();
    public static Builder builder() {
        return new AutoValue_WebsocketModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder logs(List<String> logs);
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
        public abstract Builder pausedOrPrinting(boolean pausedOrPrinting);
        public abstract Builder sdReady(boolean sdReady);
        public abstract Builder error(boolean error);
        public abstract Builder ready(boolean ready);
        public abstract Builder closedOrError(boolean closedOrError);
        public abstract Builder fileLoaded(boolean fileLoaded);

        public abstract Builder tempTime(String tempTime);
        public abstract Builder actualTempBed(float actualTempBed);
        public abstract Builder targetTempBed(float targetTempBed);
        public abstract Builder actualTempTool0(float actualTempTool0);
        public abstract Builder targetTempTool0(float targetTempTool0);
        public abstract Builder actualTempTool1(float actualTempTool1);
        public abstract Builder targetTempTool1(float targetTempTool1);

        public abstract WebsocketModel build();
    }
}
