package com.nairbspace.octoandroid.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

import java.util.ArrayList;
import java.util.List;

@AutoValue
@AutoGson(autoValueClass = AutoValue_ArbitraryCommandEntity.class)
public abstract class ArbitraryCommandEntity {
    private static final String COMMAND_MOTORS_OFF = "M18";
    private static final String COMMAND_FAN_ON = "M106";
    private static final String COMMAND_FAN_OFF = "M106 S0";

    @SerializedName("commands") public abstract List<String> commands();

    public static ArbitraryCommandEntity motorsOff() {
        List<String> commandList = new ArrayList<>();
        commandList.add(COMMAND_MOTORS_OFF);
        return new AutoValue_ArbitraryCommandEntity(commandList);
    }

    public static ArbitraryCommandEntity fanOn() {
        List<String> commandList = new ArrayList<>();
        commandList.add(COMMAND_FAN_ON);
        return new AutoValue_ArbitraryCommandEntity(commandList);
    }

    public static ArbitraryCommandEntity fanOff() {
        List<String> commandList = new ArrayList<>();
        commandList.add(COMMAND_FAN_OFF);
        return new AutoValue_ArbitraryCommandEntity(commandList);
    }

    public static ArbitraryCommandEntity createSingle(String command) {
        List<String> commandList = new ArrayList<>();
        commandList.add(command);
        return new AutoValue_ArbitraryCommandEntity(commandList);
    }

    public static ArbitraryCommandEntity create(List<String> commandList) {
        return new AutoValue_ArbitraryCommandEntity(commandList);
    }
}
