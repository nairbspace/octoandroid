package com.nairbspace.octoandroid.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ToolCommand {
    public enum Type {
        RETRACT, EXTRACT, FLOWRATE
    }

    public abstract Type command();
    public abstract int amount();
    public abstract int factor();

    public static ToolCommand createRetract(int amount) {
        return new AutoValue_ToolCommand(Type.RETRACT, amount * -1, 0);
    }

    public static ToolCommand createExtract(int amount) {
        return new AutoValue_ToolCommand(Type.EXTRACT, amount, 0);
    }

    public static ToolCommand createFlowrate(int factor) {
        return new AutoValue_ToolCommand(Type.FLOWRATE, 0, factor);
    }
}
