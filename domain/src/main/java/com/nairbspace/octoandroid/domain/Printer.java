package com.nairbspace.octoandroid.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Printer {
    public abstract Long id();

    public static Builder builder() {
        return new AutoValue_Printer.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder id(Long id);

        public abstract Printer build();
    }
}
