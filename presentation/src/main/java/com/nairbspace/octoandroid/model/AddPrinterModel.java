package com.nairbspace.octoandroid.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AddPrinterModel implements Parcelable {

    public abstract String accountName();

    public abstract String ipAddress();

    public abstract String port();

    public abstract String apiKey();

    public abstract Boolean isSslChecked();

    public static Builder builder() {
        return new AutoValue_AddPrinterModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder accountName(String accountName);

        public abstract Builder ipAddress(String ipAddress);

        public abstract Builder port(String port);

        public abstract Builder apiKey(String apiKey);

        public abstract Builder isSslChecked(Boolean isSslChecked);

        public abstract AddPrinterModel build();
    }
}
