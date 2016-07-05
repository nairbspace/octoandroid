package com.nairbspace.octoandroid.ui.temp_graph;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class LineDataSetModel {
    public abstract String label();
    public abstract int color();
    public abstract boolean actual();

    public static LineDataSetModel create(String label, int color, boolean actual) {
        return new AutoValue_LineDataSetModel.Builder()
                .label(label)
                .color(color)
                .actual(actual)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_LineDataSetModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder label(String label);
        public abstract Builder color(int color);
        public abstract Builder actual(boolean actual);
        public abstract LineDataSetModel build();
    }
}
