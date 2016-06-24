package com.nairbspace.octoandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SlicerModel implements Parcelable {
    private String key;
    private String displayName;
    private boolean isDefault;
    private List<Profile> profiles;

    public SlicerModel(String key, String displayName, boolean isDefault, List<Profile> profiles) {
        this.key = key;
        this.displayName = displayName;
        this.isDefault = isDefault;
        this.profiles = profiles;
    }

    protected SlicerModel(Parcel in) {
        key = in.readString();
        displayName = in.readString();
        isDefault = in.readByte() != 0;
        profiles = in.createTypedArrayList(Profile.CREATOR);
    }

    public static final Creator<SlicerModel> CREATOR = new Creator<SlicerModel>() {
        @Override
        public SlicerModel createFromParcel(Parcel in) {
            return new SlicerModel(in);
        }

        @Override
        public SlicerModel[] newArray(int size) {
            return new SlicerModel[size];
        }
    };

    public String key() {
        return key;
    }

    public String displayName() {
        return displayName;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public List<Profile> profiles() {
        return profiles;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(displayName);
        dest.writeByte((byte) (isDefault ? 1 : 0));
        dest.writeTypedList(profiles);
    }

    public static class Profile implements Parcelable {
        private String key;
        private String displayName;
        private boolean isDefault;
        private String resource;

        public Profile(String key, String displayName, boolean isDefault, String resource) {
            this.key = key;
            this.displayName = displayName;
            this.isDefault = isDefault;
            this.resource = resource;
        }

        protected Profile(Parcel in) {
            key = in.readString();
            displayName = in.readString();
            isDefault = in.readByte() != 0;
            resource = in.readString();
        }

        public static final Creator<Profile> CREATOR = new Creator<Profile>() {
            @Override
            public Profile createFromParcel(Parcel in) {
                return new Profile(in);
            }

            @Override
            public Profile[] newArray(int size) {
                return new Profile[size];
            }
        };

        public String key() {
            return key;
        }

        public String displayName() {
            return displayName;
        }

        public boolean isDefault() {
            return isDefault;
        }

        public String resource() {
            return resource;
        }

        @Override
        public String toString() {
            return displayName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(key);
            dest.writeString(displayName);
            dest.writeByte((byte) (isDefault ? 1 : 0));
            dest.writeString(resource);
        }
    }
}
