package com.nairbspace.octoandroid.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class FileCommand {
    public abstract String apiUrl();
    public abstract String command();
    public abstract boolean print();

    public static Builder builder() {
        return new AutoValue_FileCommand.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder apiUrl(String apiUrl);
        public abstract Builder command(String command);
        public abstract Builder print(boolean print);
        public abstract FileCommand build();
    }
}
