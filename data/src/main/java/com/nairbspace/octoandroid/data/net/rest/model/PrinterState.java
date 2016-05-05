package com.nairbspace.octoandroid.data.net.rest.model;

import com.google.gson.annotations.SerializedName;

public class PrinterState {
    @SerializedName("sd") public Sd sd;
    @SerializedName("state") public State state;
    @SerializedName("temperature") public Temperature temperature;

    public State getState() {
        return state;
    }

    public class Sd {
        @SerializedName("ready") public Boolean ready;
    }

    public class State {
        @SerializedName("flags") public Flags flags;
        @SerializedName("text") public String text;

        public String getText() {
            return text;
        }

        public class Flags {
            @SerializedName("closedOrError") public Boolean closedOrError;
            @SerializedName("error") public Boolean error;
            @SerializedName("operational") public Boolean operational;
            @SerializedName("paused") public Boolean paused;
            @SerializedName("printing") public Boolean printing;
            @SerializedName("ready") public Boolean ready;
            @SerializedName("sdReady") public Boolean sdReady;
        }
    }

    public class Temperature {
        @SerializedName("bed") public Bed bed;
        @SerializedName("tool0") public Tool0 tool0;
        @SerializedName("tool1") public Tool1 tool1;

        public class Bed {
            @SerializedName("actual") public Integer actual;
            @SerializedName("offset") public Integer offset;
            @SerializedName("target") public Integer target;
        }

        public class Tool0 {
            @SerializedName("actual") public Integer actual;
            @SerializedName("offset") public Integer offset;
            @SerializedName("target") public Integer target;
        }

        public class Tool1 {
            @SerializedName("actual") public Integer actual;
            @SerializedName("offset") public Integer offset;
            @SerializedName("target") public Integer target;
        }
    }
}
