package com.nairbspace.octoandroid.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PrintHeadCommand {
    public enum Type {
        JOG_X_LEFT, JOG_X_RIGHT,
        JOG_Y_DOWN, JOG_Y_UP,
        JOG_Z_DOWN, JOG_Z_UP,
        HOME_XY, HOME_Z,
        FEEDRATE
    }

    public abstract PrintHeadCommand.Type type();
    public abstract float jog();
    public abstract int factor();

    public static PrintHeadCommand createJog(PrintHeadCommand.Type type, float jog) {
        return new AutoValue_PrintHeadCommand(type, jog, 0);
    }

    public static PrintHeadCommand createHome(PrintHeadCommand.Type type) {
        return new AutoValue_PrintHeadCommand(type, 0f, 0);
    }

    public static PrintHeadCommand createFeedRate(PrintHeadCommand.Type type, int factor) {
        return new AutoValue_PrintHeadCommand(type, 0f, factor);
    }
}
