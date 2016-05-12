package com.nairbspace.octoandroid.model;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class ConnectionModel {
    public abstract boolean isNotConnected();
    public abstract List<String> ports();
    public abstract int defaultPortId();
    public abstract List<Integer> baudrates();
    public abstract int defaultBaudrateId();
    public abstract List<String> printerProfileIds();
    public abstract List<String> printerProfileNames();
    public abstract int defaultPrinterProfileId();

    public static Builder builder() {
        return new AutoValue_ConnectionModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder isNotConnected(boolean isNotConnected);

        public abstract Builder ports(List<String> ports);

        public abstract Builder defaultPortId(int defaultPortId);

        public abstract Builder baudrates(List<Integer> baudrates);

        public abstract Builder defaultBaudrateId(int defaultBaudrateId);

        public abstract Builder printerProfileIds(List<String> printerProfileIds);

        public abstract Builder printerProfileNames(List<String> printerProfileNames);

        public abstract Builder defaultPrinterProfileId(int defaultPrinterProfileId);

        public abstract ConnectionModel build();
    }
}
