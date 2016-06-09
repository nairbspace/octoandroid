package com.nairbspace.octoandroid.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

public abstract class ToolCommandEntity {
    private static final String COMMAND_SELECT = "select";
    private static final String TOOL0 = "tool0";
    private static final String TOOL1 = "tool1";
    private static final String COMMAND_EXTRUDE = "extrude";
    private static final String COMMAND_FLOWRATE = "flowrate";

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_ToolCommandEntity_Select.class)
    public static abstract class Select extends ToolCommandEntity {
        @SerializedName("command") public abstract String command();
        @SerializedName("tool") public abstract String tool();

        public static Select createTool0() {
            return new AutoValue_ToolCommandEntity_Select(COMMAND_SELECT, TOOL0);
        }

        public static Select createTool1() {
            return new AutoValue_ToolCommandEntity_Select(COMMAND_SELECT, TOOL1);
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_ToolCommandEntity_Extract.class)
    public static abstract class Extract extends ToolCommandEntity {
        @SerializedName("command") public abstract String command();
        @SerializedName("amount") public abstract int amount();

        public static Extract create(int amount) {
            return new AutoValue_ToolCommandEntity_Extract(COMMAND_EXTRUDE, amount);
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_ToolCommandEntity_Flowrate.class)
    public abstract static class Flowrate extends ToolCommandEntity {
        @SerializedName("command") public abstract String command();
        @SerializedName("factor") public abstract int factor();

        public static Flowrate create(int factor) {
            return new AutoValue_ToolCommandEntity_Flowrate(COMMAND_FLOWRATE, factor);
        }
    }
}
