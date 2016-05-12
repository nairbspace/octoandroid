package com.nairbspace.octoandroid.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
@AutoGson(autoValueClass = AutoValue_Connection.class)
public abstract class Connection {
    @SerializedName("current") public abstract Current current();
    @SerializedName("options") public abstract Options options();

//    public static TypeAdapter<Connection> typeAdapter(Gson gson) {
//        return new AutoValue_Connection.GsonTypeAdapter(gson);
//    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_Connection_Current.class)
    public abstract static class Current {
        @Nullable @SerializedName("baudrate") public abstract Integer baudrate();
        @Nullable @SerializedName("port") public abstract String port();
        @SerializedName("printerProfile") public abstract String printerProfile();
        @SerializedName("state") public abstract String state();

//        public static TypeAdapter<Current> typeAdapter(Gson gson) {
//            return new AutoValue_Connection_Current.GsonTypeAdapter(gson);
//        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_Connection_Options.class)
    public abstract static class Options {
        @SerializedName("baudratePreference") public abstract Integer baudratePreference();
        @SerializedName("baudrates") public abstract List<Integer> baudrates();
        @SerializedName("portPreference") public abstract String portPreference();
        @SerializedName("ports") public abstract List<String> ports();
        @SerializedName("printerProfilePreference") public abstract String printerProfilePreference();
        @SerializedName("printerProfiles") public abstract List<PrinterProfile> printerProfiles();
        @SerializedName("autoconnect") public abstract boolean autoconnect();

//        public static TypeAdapter<Options> typeAdapter(Gson gson) {
//            return new AutoValue_Connection_Options.GsonTypeAdapter(gson);
//        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_Connection_PrinterProfile.class)
    public abstract static class PrinterProfile {
        @SerializedName("printerDetails") public abstract String id();
        @SerializedName("name") public abstract String name();

//        public static TypeAdapter<PrinterProfile> typeAdapter(Gson gson) {
//            return new AutoValue_Connection_PrinterProfile.GsonTypeAdapter(gson);
//        }
    }
}
