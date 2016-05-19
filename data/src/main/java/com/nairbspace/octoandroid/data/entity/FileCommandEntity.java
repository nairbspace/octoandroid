package com.nairbspace.octoandroid.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

@AutoValue
@AutoGson(autoValueClass = AutoValue_FileCommandEntity.class)
public abstract class FileCommandEntity {
    @SerializedName("command") public abstract String command();
    @SerializedName("print") public abstract boolean print();

    public static Builder builder() {
        return new AutoValue_FileCommandEntity.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder command(String command);
        public abstract Builder print(boolean print);
        public abstract FileCommandEntity build();
    }
}
