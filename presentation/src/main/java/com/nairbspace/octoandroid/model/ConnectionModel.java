package com.nairbspace.octoandroid.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

import java.util.List;

@AutoValue
@AutoGson(autoValueClass = AutoValue_ConnectionModel.class)
public abstract class ConnectionModel {
    @SerializedName("current") public abstract Current current();
    @SerializedName("options") public abstract Options options();

//    public static TypeAdapter<Connection> typeAdapter(Gson gson) {
//        return new AutoValue_ConnectionModel.GsonTypeAdapter(gson);
//    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_ConnectionModel_Current.class)
    public abstract static class Current {
        @Nullable @SerializedName("baudrate") public abstract Integer baudrate();
        @Nullable @SerializedName("port") public abstract String port();
        @SerializedName("printerProfile") public abstract String printerProfile();
        @SerializedName("state") public abstract String state();

//        public static TypeAdapter<Current> typeAdapter(Gson gson) {
//            return new AutoValue_ConnectionModel_Current.GsonTypeAdapter(gson);
//        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_ConnectionModel_Options.class)
    public abstract static class Options {
        @SerializedName("baudratePreference") public abstract Integer baudratePreference();
        @SerializedName("baudrates") public abstract List<Integer> baudrates();
        @SerializedName("portPreference") public abstract String portPreference();
        @SerializedName("ports") public abstract List<String> ports();
        @SerializedName("printerProfilePreference") public abstract String printerProfilePreference();
        @SerializedName("printerProfiles") public abstract List<PrinterProfile> printerProfiles();

//        public static TypeAdapter<Options> typeAdapter(Gson gson) {
//            return new AutoValue_ConnectionModel_Options.GsonTypeAdapter(gson);
//        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_ConnectionModel_PrinterProfile.class)
    public abstract static class PrinterProfile {
        @SerializedName("printerDetails") public abstract String id();
        @SerializedName("name") public abstract String name();

//        public static TypeAdapter<PrinterProfile> typeAdapter(Gson gson) {
//            return new AutoValue_ConnectionModel_PrinterProfile.GsonTypeAdapter(gson);
//        }
    }
}
