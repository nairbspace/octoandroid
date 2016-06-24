package com.nairbspace.octoandroid.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Spinner;

/**
 * Immutable model used to keep key/value synced when displaying in spinner.
 * Spinner depends on toString to display to user. When wanting to get key back
 * call {@link Spinner#getSelectedItem()} method from spinner and cast to {@link SpinnerModel}.
 */
public final class SpinnerModel implements Parcelable {

    private String id;
    private String name;

    public SpinnerModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected SpinnerModel(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<SpinnerModel> CREATOR = new Creator<SpinnerModel>() {
        @Override
        public SpinnerModel createFromParcel(Parcel in) {
            return new SpinnerModel(in);
        }

        @Override
        public SpinnerModel[] newArray(int size) {
            return new SpinnerModel[size];
        }
    };

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}
