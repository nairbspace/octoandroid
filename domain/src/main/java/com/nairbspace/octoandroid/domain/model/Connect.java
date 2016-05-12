package com.nairbspace.octoandroid.domain.model;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Connect {
    public abstract boolean isNotConnected();
    public abstract List<String> ports();
    public abstract List<Integer> baudrates();
    public abstract List<String> printerProfileNames();
    public abstract int portId();
    public abstract int baudrateId();
    public abstract int printerNameId();
    public abstract boolean isSaveConnectionChecked();
    public abstract boolean isAutoConnectChecked();

    public static Builder builder() {
        return new AutoValue_Connect.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder isNotConnected(boolean isNotConnected);
        public abstract Builder ports(List<String> ports);
        public abstract Builder baudrates(List<Integer> baudrates);
        public abstract Builder printerProfileNames(List<String> printerProfileNames);
        public abstract Builder portId(int portId);
        public abstract Builder baudrateId(int baudrateId);
        public abstract Builder printerNameId(int printerNameId);
        public abstract Builder isSaveConnectionChecked(boolean isSaveConnectionChecked);
        public abstract Builder isAutoConnectChecked(boolean isAutoConnectChecked);
        public abstract Connect build();
    }
}
