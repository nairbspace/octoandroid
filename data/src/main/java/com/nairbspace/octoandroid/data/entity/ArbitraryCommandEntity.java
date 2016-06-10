package com.nairbspace.octoandroid.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

import java.util.List;

public abstract class ArbitraryCommandEntity {
    private static final String COMMAND_MOTORS_OFF = "M18";
    private static final String COMMAND_FAN_ON = "M105";
    private static final String COMMAND_FAN_OFF = "M106 S0";

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_ArbitraryCommandEntity_Single.class)
    public static abstract class Single extends ArbitraryCommandEntity {
        @SerializedName("command") public abstract String command();

        public static Single motorsOff() {
            return new AutoValue_ArbitraryCommandEntity_Single(COMMAND_MOTORS_OFF);
        }

        public static Single fanOn() {
            return new AutoValue_ArbitraryCommandEntity_Single(COMMAND_FAN_ON);
        }

        public static Single fanOff() {
            return new AutoValue_ArbitraryCommandEntity_Single(COMMAND_FAN_OFF);
        }

        public static Single create(String s) {
            return new AutoValue_ArbitraryCommandEntity_Single(s);
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_ArbitraryCommandEntity_Multiple.class)
    public static abstract class Multiple extends ArbitraryCommandEntity {
        @SerializedName("commands") public abstract List<String> commands();

        public static Multiple create(List<String> strings) {
            return new AutoValue_ArbitraryCommandEntity_Multiple(strings);
        }
    }
}
