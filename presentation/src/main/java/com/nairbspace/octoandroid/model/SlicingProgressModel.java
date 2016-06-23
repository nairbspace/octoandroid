package com.nairbspace.octoandroid.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SlicingProgressModel {
    public abstract String slicer();
    public abstract String sourceLocation();
    public abstract String sourcePath();
    public abstract String destLocation();
    public abstract String destPath();
    public abstract int progress();

    public static SlicingProgressModel initial() {
        return new AutoValue_SlicingProgressModel.Builder()
                .slicer("")
                .sourceLocation("")
                .sourcePath("")
                .destLocation("")
                .destPath("")
                .progress(0)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_SlicingProgressModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder slicer(String slicer);
        public abstract Builder sourceLocation(String sourceLocation);
        public abstract Builder sourcePath(String sourcePath);
        public abstract Builder destLocation(String destLocation);
        public abstract Builder destPath(String destPath);
        public abstract Builder progress(int progress);
        public abstract SlicingProgressModel build();
    }
}
