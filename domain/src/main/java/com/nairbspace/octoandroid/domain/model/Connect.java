package com.nairbspace.octoandroid.domain.model;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Connect {
    public abstract boolean isNotConnected();
    public abstract List<String> ports();
    public abstract List<Integer> baudrates();
    public abstract List<String> printerProfileIds();
    public abstract int portId();
    public abstract int baudrateId();
    public abstract int printerProfileId();
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
        public abstract Builder printerProfileIds(List<String> printerProfileIds);
        public abstract Builder portId(int portId);
        public abstract Builder baudrateId(int baudrateId);
        public abstract Builder printerProfileId(int printerProfileId);
        public abstract Builder isSaveConnectionChecked(boolean isSaveConnectionChecked);
        public abstract Builder isAutoConnectChecked(boolean isAutoConnectChecked);
        public abstract Connect build();
    }
}
