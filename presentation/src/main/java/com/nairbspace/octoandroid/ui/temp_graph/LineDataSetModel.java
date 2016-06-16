package com.nairbspace.octoandroid.ui.temp_graph;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class LineDataSetModel {
    public abstract String label();
    public abstract int color();
    public abstract int index();
    public abstract boolean actual();

    public static LineDataSetModel create(String label, int color, boolean actual) {
        return new AutoValue_LineDataSetModel.Builder()
                .label(label)
                .color(color)
                .index(-1)
                .actual(actual)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_LineDataSetModel.Builder();
    }

    abstract Builder toBuilder();

    public LineDataSetModel withIndex(int index) {
        return toBuilder().index(index).build();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder label(String label);
        public abstract Builder color(int color);
        public abstract Builder index(int index);
        public abstract Builder actual(boolean actual);
        public abstract LineDataSetModel build();
    }
}
