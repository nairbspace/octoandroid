package com.nairbspace.octoandroid.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class SlicerModel implements Parcelable {
    public abstract String key();
    public abstract String displayName();
    public abstract boolean isDefault();
    public abstract List<Profile> profiles();

    public static Builder builder() {
        return new AutoValue_SlicerModel.Builder();
    }

    @Override
    public String toString() {
        return displayName(); // Need to only display name when used in spinner
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder key(String key);
        public abstract Builder displayName(String displayName);
        public abstract Builder isDefault(boolean isDefault);
        public abstract Builder profiles(List<Profile> profiles);
        public abstract SlicerModel build();
    }

    @AutoValue
    public static abstract class Profile implements Parcelable {
        public abstract String key();
        public abstract String displayName();
        public abstract boolean isDefault();
        public abstract String resource();

        public static Builder builder() {
            return new AutoValue_SlicerModel_Profile.Builder();
        }

        @Override
        public String toString() {
            return displayName();
        }

        @AutoValue.Builder
        public static abstract class Builder {
            public abstract Builder key(String key);
            public abstract Builder displayName(String displayName);
            public abstract Builder isDefault(boolean isDefault);
            public abstract Builder resource(String resource);
            public abstract Profile build();
        }
    }
}
