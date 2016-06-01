package com.nairbspace.octoandroid.ui.temp_controls;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TempControlsModel implements Parcelable {
    public abstract String actualTempTool0();
    public abstract String targetTempTool0();
    public abstract String offsetTempTool0();

    public abstract String actualTempTool1();
    public abstract String targetTempTool1();
    public abstract String offsetTempTool1();

    public abstract String actualTempBed();
    public abstract String targetTempBed();
    public abstract String offsetTempBed();

    public static Builder builder() {
        return new AutoValue_TempControlsModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder actualTempTool0(String actualTempTool0);
        public abstract Builder targetTempTool0(String targetTempTool0);
        public abstract Builder offsetTempTool0(String offsetTempTool0);

        public abstract Builder actualTempTool1(String actualTempTool1);
        public abstract Builder targetTempTool1(String targetTempTool1);
        public abstract Builder offsetTempTool1(String offsetTempTool1);

        public abstract Builder actualTempBed(String actualTempBed);
        public abstract Builder targetTempBed(String targetTempBed);
        public abstract Builder offsetTempBed(String offsetTempBed);

        public abstract TempControlsModel build();
    }
}
