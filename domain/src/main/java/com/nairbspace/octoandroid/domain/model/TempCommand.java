package com.nairbspace.octoandroid.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TempCommand {
    public enum ToolBedOffsetTemp {
        TARGET_TOOL0, OFFSET_TOOL0,
        TARGET_TOOL1, OFFSET_TOOL1,
        TARGET_BED, OFFSET_BED
    }

    public abstract ToolBedOffsetTemp toolBedOffsetTemp();
    public abstract int temp();

    public static TempCommand create(ToolBedOffsetTemp toolBedOffsetTemp, int temp) {
        return new AutoValue_TempCommand(toolBedOffsetTemp, temp);
    }
}
