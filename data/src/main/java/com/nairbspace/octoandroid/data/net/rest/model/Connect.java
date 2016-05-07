package com.nairbspace.octoandroid.data.net.rest.model;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Connect {
    public static final String COMMAND_CONNECT = "connect";

    @SerializedName("command") public abstract String command();
    @SerializedName("port") public abstract String port();
    @SerializedName("baudrate") public abstract Integer baudrate();
    @SerializedName("printerProfile") public abstract String printerProfile();
    @SerializedName("save") public abstract Boolean save();
    @SerializedName("autoconnect") public abstract Boolean autoconnect();

    public static Builder builder() {
        return new AutoValue_Connect.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder command(String command);
        public abstract Builder port(String port);
        public abstract Builder baudrate(Integer baudrate);
        public abstract Builder printerProfile(String printerProfile);
        public abstract Builder save(Boolean save);
        public abstract Builder autoconnect(Boolean autoconnect);
        public abstract Connect build();
    }
}
