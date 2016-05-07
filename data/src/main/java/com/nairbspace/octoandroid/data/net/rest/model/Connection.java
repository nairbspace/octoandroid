package com.nairbspace.octoandroid.data.net.rest.model;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class Connection {
    @SerializedName("current") public abstract Current current();
    @SerializedName("options") public abstract Options options();

    @AutoValue
    public abstract static class Current {
        @SerializedName("baudrate") public abstract Integer baudrate();
        @SerializedName("port") public abstract String port();
        @SerializedName("printerProfile") public abstract String printerProfile();
        @SerializedName("state") public abstract String state();
    }

    @AutoValue
    public abstract static class Options {
        @SerializedName("baudratePreference") public abstract Integer baudratePreference();
        @SerializedName("baudrates") public abstract List<Integer> baudrates();
        @SerializedName("portPreference") public abstract String portPreference();
        @SerializedName("ports") public abstract List<String> ports();
        @SerializedName("printerProfilePreference") public abstract String printerProfilePreference();
        @SerializedName("printerProfiles") public abstract List<PrinterProfile> printerProfiles();
    }

    @AutoValue
    public abstract static class PrinterProfile {
        @SerializedName("id") public abstract String id();
        @SerializedName("name") public abstract String name();
    }
}
