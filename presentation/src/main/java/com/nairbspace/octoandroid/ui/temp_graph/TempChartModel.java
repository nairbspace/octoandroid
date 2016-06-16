package com.nairbspace.octoandroid.ui.temp_graph;

import android.os.Parcelable;

import com.github.mikephil.charting.data.Entry;
import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class TempChartModel implements Parcelable {
    public abstract ArrayList<String> xValsTime();
    public abstract List<Entry> yActualTempBed();
    public abstract List<Entry> yTargetTempBed();
    public abstract List<Entry> yActualTempTool0();
    public abstract List<Entry> yTargetTempTool0();
    public abstract List<Entry> yActualTempTool1();
    public abstract List<Entry> yTargetTempTool1();

    public static Builder builder() {
        return new AutoValue_TempChartModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder xValsTime(ArrayList<String> xValsTime);
        public abstract Builder yActualTempBed(List<Entry> yActualTempBed);
        public abstract Builder yTargetTempBed(List<Entry> yTargetTempBed);
        public abstract Builder yActualTempTool0(List<Entry> yActualTempTool0);
        public abstract Builder yTargetTempTool0(List<Entry> yTargetTempTool0);
        public abstract Builder yActualTempTool1(List<Entry> yActualTempTool1);
        public abstract Builder yTargetTempTool1(List<Entry> yTargetTempTool1);
        public abstract TempChartModel build();
    }
}
