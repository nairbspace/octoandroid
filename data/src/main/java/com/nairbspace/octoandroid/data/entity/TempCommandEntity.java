package com.nairbspace.octoandroid.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

public class TempCommandEntity {
    private static final String COMMAND_TARGET = "target";
    private static final String COMMAND_OFFSET = "offset";

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_TempCommandEntity_TargetTool0.class)
    public static abstract class TargetTool0 {
        @SerializedName("command") public abstract String command();
        @SerializedName("targets") public abstract Targets targets();

        public static TargetTool0 create(int targetTemp) {
            Targets targets = Targets.create(targetTemp);
            return new AutoValue_TempCommandEntity_TargetTool0(COMMAND_TARGET, targets);
        }

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_TempCommandEntity_TargetTool0_Targets.class)
        public static abstract class Targets {
            @SerializedName("tool0") public abstract int tool0();

            public static Targets create(int targetTemp) {
                return new AutoValue_TempCommandEntity_TargetTool0_Targets(targetTemp);
            }
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_TempCommandEntity_OffsetTool0.class)
    public static abstract class OffsetTool0 {
        @SerializedName("command") public abstract String command();
        @SerializedName("offsets") public abstract Offsets offsets();

        public static OffsetTool0 create(int offsetTemp) {
            Offsets offsets = Offsets.create(offsetTemp);
            return new AutoValue_TempCommandEntity_OffsetTool0(COMMAND_OFFSET, offsets);
        }

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_TempCommandEntity_OffsetTool0_Offsets.class)
        public static abstract class Offsets {
            @SerializedName("tool0") public abstract int tool0();

            public static Offsets create(int offsetTemp) {
                return new AutoValue_TempCommandEntity_OffsetTool0_Offsets(offsetTemp);
            }
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_TempCommandEntity_TargetTool1.class)
    public static abstract class TargetTool1 {
        @SerializedName("command") public abstract String command();
        @SerializedName("targets") public abstract Targets targets();

        public static TargetTool1 create(int targetTemp) {
            Targets targets = Targets.create(targetTemp);
            return new AutoValue_TempCommandEntity_TargetTool1(COMMAND_TARGET, targets);
        }

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_TempCommandEntity_TargetTool1_Targets.class)
        public static abstract class Targets {
            @SerializedName("tool1") public abstract int tool1();

            public static Targets create(int targetTemp) {
                return new AutoValue_TempCommandEntity_TargetTool1_Targets(targetTemp);
            }
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_TempCommandEntity_OffsetTool1.class)
    public static abstract class OffsetTool1 {
        @SerializedName("command") public abstract String command();
        @SerializedName("offsets") public abstract Offsets offsets();

        public static OffsetTool1 create(int offsetTemp) {
            Offsets offsets = Offsets.create(offsetTemp);
            return new AutoValue_TempCommandEntity_OffsetTool1(COMMAND_OFFSET, offsets);
        }

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_TempCommandEntity_OffsetTool1_Offsets.class)
        public static abstract class Offsets {
            @SerializedName("tool1") public abstract int tool0();

            public static Offsets create(int offsetTemp) {
                return new AutoValue_TempCommandEntity_OffsetTool1_Offsets(offsetTemp);
            }
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_TempCommandEntity_TargetBed.class)
    public static abstract class TargetBed {
        @SerializedName("command") public abstract String command();
        @SerializedName("target") public abstract int target();

        public static TargetBed create(int targetTemp) {
            return new AutoValue_TempCommandEntity_TargetBed(COMMAND_TARGET, targetTemp);
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_TempCommandEntity_OffsetBed.class)
    public static abstract class OffsetBed {
        @SerializedName("command") public abstract String command();
        @SerializedName("offset") public abstract int offset();

        public static OffsetBed create(int offsetTemp) {
            return new AutoValue_TempCommandEntity_OffsetBed(COMMAND_OFFSET, offsetTemp);
        }
    }
}
