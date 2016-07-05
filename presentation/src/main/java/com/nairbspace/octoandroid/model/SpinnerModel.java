package com.nairbspace.octoandroid.model;

import android.os.Parcelable;
import android.widget.Spinner;

import com.google.auto.value.AutoValue;

/**
 * Immutable model used to keep key/value synced when displaying in spinner.
 * Spinner depends on toString to display to user. When wanting to get key back
 * call {@link Spinner#getSelectedItem()} method from spinner and cast to {@link SpinnerModel}.
 */
@AutoValue
public abstract class SpinnerModel implements Parcelable {

    public abstract String id();
    public abstract String name();

    public static SpinnerModel create(String id, String name) {
        return new AutoValue_SpinnerModel(id, name);
    }

    @Override
    public String toString() {
        return name();
    }
}
