package com.nairbspace.octoandroid.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class FileCommandModel {
    private static final boolean START_PRINT = true;
    private static final boolean LOAD_PRINT = false;
    private static final String COMMAND_SELECT = "select";

    public abstract String apiUrl();
    public abstract String command();
    public abstract boolean print();

    public static FileCommandModel startPrint(String apiUrl) {
        return new AutoValue_FileCommandModel.Builder()
                .apiUrl(apiUrl)
                .command(COMMAND_SELECT)
                .print(START_PRINT)
                .build();
    }

    public static FileCommandModel loadPrint(String apiUrl) {
        return new AutoValue_FileCommandModel.Builder()
                .apiUrl(apiUrl)
                .command(COMMAND_SELECT)
                .print(LOAD_PRINT)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_FileCommandModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder apiUrl(String apiUrl);
        public abstract Builder command(String command);
        public abstract Builder print(boolean print);
        public abstract FileCommandModel build();
    }
}
