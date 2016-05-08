package com.nairbspace.octoandroid.data.entity;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue
@AutoGson(autoValueClass = AutoValue_ConnectEntity.class)
public abstract class ConnectEntity {
    public static final String COMMAND_CONNECT = "connect";
    public static final String COMMAND_DISCONNECT = "disconnect";

    @SerializedName("command") public abstract String command();
    @Nullable @SerializedName("port") public abstract String port();
    @Nullable @SerializedName("baudrate") public abstract Integer baudrate();
    @Nullable @SerializedName("printerProfile") public abstract String printerProfile();
    @Nullable @SerializedName("save") public abstract Boolean save();
    @Nullable @SerializedName("autoconnect") public abstract Boolean autoconnect();

//    public static TypeAdapter<Connect> typeAdapter(Gson gson) {
//        return new AutoValue_Connect.GsonTypeAdapter(gson);
//    }

    public static Builder builder() {
        return new AutoValue_ConnectEntity.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder command(String command);
        public abstract Builder port(String port);
        public abstract Builder baudrate(Integer baudrate);
        public abstract Builder printerProfile(String printerProfile);
        public abstract Builder save(Boolean save);
        public abstract Builder autoconnect(Boolean autoconnect);
        public abstract ConnectEntity build();
    }
}
