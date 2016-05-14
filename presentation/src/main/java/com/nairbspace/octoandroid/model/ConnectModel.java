package com.nairbspace.octoandroid.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.HashMap;
import java.util.List;

@AutoValue
public abstract class ConnectModel implements Parcelable {
    public abstract boolean isNotConnected();
    public abstract List<String> ports();
    public abstract List<Integer> baudrates();
    public abstract HashMap<String, String> printerProfiles();
    public abstract int selectedPortId();
    public abstract int selectedBaudrateId();
    public abstract int selectedPrinterProfileId();
    public abstract boolean isSaveConnectionChecked();
    public abstract boolean isAutoConnectChecked();

    public static Builder builder() {
        return new AutoValue_ConnectModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder isNotConnected(boolean isNotConnected);
        public abstract Builder ports(List<String> ports);
        public abstract Builder baudrates(List<Integer> baudrates);
        public abstract Builder printerProfiles(HashMap<String, String> printerProfiles);
        public abstract Builder selectedPortId(int selectedPortId);
        public abstract Builder selectedBaudrateId(int selectedBaudrateId);
        public abstract Builder selectedPrinterProfileId(int selectedPrinterProfileId);
        public abstract Builder isSaveConnectionChecked(boolean isSaveConnectionChecked);
        public abstract Builder isAutoConnectChecked(boolean isAutoConnectChecked);
        public abstract ConnectModel build();
    }
}
