package com.nairbspace.octoandroid.data.net.rest.model;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class PrinterState {
    @SerializedName("sd") public abstract Sd sd();
    @SerializedName("state") public abstract State state();
    @SerializedName("temperature") public abstract Temperature temperature();

    @AutoValue
    public static abstract class Sd {
        @SerializedName("ready") public abstract Boolean ready();
    }

    @AutoValue
    public static abstract class State {
        @SerializedName("flags") public abstract Flags flags();
        @SerializedName("text") public abstract String text();

        @AutoValue public static abstract class Flags {
            @SerializedName("closedOrError") public abstract Boolean closedOrError();
            @SerializedName("error") public abstract Boolean error();
            @SerializedName("operational") public abstract Boolean operational();
            @SerializedName("paused") public abstract Boolean paused();
            @SerializedName("printing") public abstract Boolean printing();
            @SerializedName("ready") public abstract Boolean ready();
            @SerializedName("sdReady") public abstract Boolean sdReady();
        }
    }

    @AutoValue
    public static abstract class Temperature {
        @SerializedName("bed") public abstract Bed bed();
        @SerializedName("tool0") public abstract Tool0 tool0();
        @SerializedName("tool1") public abstract Tool1 tool1();

        @AutoValue
        public static abstract class Bed {
            @SerializedName("actual") public abstract Integer actual();
            @SerializedName("offset") public abstract Integer offset();
            @SerializedName("target") public abstract Integer target();
        }

        @AutoValue
        public static abstract class Tool0 {
            @SerializedName("actual") public abstract Integer actual();
            @SerializedName("offset") public abstract Integer offset();
            @SerializedName("target") public abstract Integer target();
        }

        @AutoValue
        public static abstract class Tool1 {
            @SerializedName("actual") public abstract Integer actual();
            @SerializedName("offset") public abstract Integer offset();
            @SerializedName("target") public abstract Integer target();
        }
    }
}
