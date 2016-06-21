package com.nairbspace.octoandroid.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

import java.util.Map;

@AutoValue
@AutoGson(autoValueClass = AutoValue_SlicerModel.class)
public abstract class SlicerModel implements Parcelable {
    @Nullable @SerializedName("key") public abstract String key();
    @Nullable @SerializedName("displayName") public abstract String displayName();
    @Nullable @SerializedName("default") public abstract Boolean isDefault();
    @Nullable @SerializedName("profiles") public abstract Map<String, Profile> profiles();

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_SlicerModel_Profile.class)
    public static abstract class Profile implements Parcelable {
        @Nullable @SerializedName("key") public abstract String key();
        @Nullable @SerializedName("displayName") public abstract String displayName();
        @Nullable @SerializedName("default") public abstract Boolean isDefault();
        @Nullable @SerializedName("resource") public abstract String resource();
    }
}
