package com.nairbspace.octoandroid.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

import java.util.ArrayList;
import java.util.List;

public abstract class PrintHeadCommandEntity {
    private static final String COMMAND_JOG = "jog";
    private static final String COMMAND_HOME = "home";
    private static final String COMMAND_FEEDRATE = "feedrate";

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_PrintHeadCommandEntity_Jog.class)
    public static abstract class Jog extends PrintHeadCommandEntity {
        @SerializedName("command") public abstract String command();
        @SerializedName("x") public abstract float x();
        @SerializedName("y") public abstract float y();
        @SerializedName("z") public abstract float z();

        public static Jog createX(float x) {
            return new AutoValue_PrintHeadCommandEntity_Jog(COMMAND_JOG, x, 0f, 0f);
        }

        public static Jog createY(float y) {
            return new AutoValue_PrintHeadCommandEntity_Jog(COMMAND_JOG, 0f, y, 0f);
        }

        public static Jog createZ(float z) {
            return new AutoValue_PrintHeadCommandEntity_Jog(COMMAND_JOG, 0f, 0f, z);
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_PrintHeadCommandEntity_Home.class)
    public static abstract class Home extends PrintHeadCommandEntity {
        private static final String X_AXIS = "x";
        private static final String Y_AXIS = "y";
        private static final String Z_AXES = "z";

        @SerializedName("command") public abstract String command();
        @SerializedName("axes") public abstract List<String> axes();

        public static Home createXy() {
            List<String> axes = new ArrayList<>();
            axes.add(X_AXIS);
            axes.add(Y_AXIS);
            return new AutoValue_PrintHeadCommandEntity_Home(COMMAND_HOME, axes);
        }

        public static Home createZ() {
            List<String> axes = new ArrayList<>();
            axes.add(Z_AXES);
            return new AutoValue_PrintHeadCommandEntity_Home(COMMAND_HOME, axes);
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_PrintHeadCommandEntity_FeedRate.class)
    public static abstract class FeedRate extends PrintHeadCommandEntity {

        @SerializedName("command") public abstract String command();
        @SerializedName("factor") public abstract int factor();

        public static FeedRate create(int factor) {
            return new AutoValue_PrintHeadCommandEntity_FeedRate(COMMAND_FEEDRATE, factor);
        }
    }
}
