package com.nairbspace.octoandroid.domain.model;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class ArbitraryCommand {
    public enum Type {
        MOTORS_OFF, FAN_ON, FAN_OFF, SINGLE, MULTIPLE
    }

    public abstract Type type();
    public abstract String command();
    public abstract List<String> commands();

    public static ArbitraryCommand createGeneral(Type type) {
        return new AutoValue_ArbitraryCommand(type, "", new ArrayList<String>());
    }

    public static ArbitraryCommand createSingle(Type type, String string) {
        return new AutoValue_ArbitraryCommand(type, string, new ArrayList<String>());
    }

    public static ArbitraryCommand createMultiple(Type type, List<String> strings) {
        return new AutoValue_ArbitraryCommand(type, "", strings);
    }
}
