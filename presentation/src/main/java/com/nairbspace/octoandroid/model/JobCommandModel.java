package com.nairbspace.octoandroid.model;

import android.support.annotation.NonNull;

import java.util.HashMap;

public final class JobCommandModel {

    public enum CommandType {
        START, RESTART, PAUSE, RESUME, CANCEL
    }

    private static final String COMMAND = "command";
    private static final String START = "start";
    private static final String RESTART = "restart";
    private static final String PAUSE = "pause";
    private static final String CANCEL = "cancel";

    public static HashMap<String, String> startCommand() {
        return hashMapBuilder(START);
    }

    public static HashMap<String, String> restartCommand() {
        return hashMapBuilder(RESTART);
    }

    public static HashMap<String, String> pauseCommand() {
        return hashMapBuilder(PAUSE);
    }

    public static HashMap<String, String> resumeCommand() {
        return hashMapBuilder(PAUSE);
    }

    public static HashMap<String, String> cancelCommand() {
        return hashMapBuilder(CANCEL);
    }

    public static HashMap<String, String> getCommand(@NonNull CommandType commandType) {
        switch (commandType) {
            case START:
                return startCommand();
            case RESTART:
                return restartCommand();
            case PAUSE:
                return pauseCommand();
            case RESUME:
                return resumeCommand();
            case CANCEL:
                return cancelCommand();
            default:
                return pauseCommand();
        }
    }

    private static HashMap<String, String> hashMapBuilder(String command) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(COMMAND, command);
        return hashMap;
    }
}
