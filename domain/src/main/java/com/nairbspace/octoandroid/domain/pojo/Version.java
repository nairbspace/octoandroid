package com.nairbspace.octoandroid.domain.pojo;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Version {
    public abstract String api();
    public abstract String server();

    public static Builder builder() {
        return new AutoValue_Version.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder api(String api);
        public abstract Builder server(String server);
        public abstract Version build();
    }
}
