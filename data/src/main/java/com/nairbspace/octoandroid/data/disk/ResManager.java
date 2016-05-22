package com.nairbspace.octoandroid.data.disk;

import android.content.res.Resources;

import com.nairbspace.octoandroid.data.R;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ResManager {

    private final Resources mResources;

    @Inject
    public ResManager(Resources resources) {
        mResources = resources;
    }

    public long getNoActivePrinterValue() {
        String noActivePrinterString = mResources.getString(R.string.prefs_active_printer_none_value);
        return Long.parseLong(noActivePrinterString);
    }

    public String getActivePrinterKey() {
        return mResources.getString(R.string.prefs_active_printer_key);
    }

    public String getLastSaveTimeKey() {
        return mResources.getString(R.string.prefs_last_save_time_key);
    }

    public int getDefaultSaveTimeValue() {
        return mResources.getInteger(R.integer.prefs_last_save_time_default_value);
    }

    public String getUploadLocationKey() {
        return mResources.getString(R.string.prefs_upload_location_key);
    }

    public String getDefaultUploadLocationValue() {
        return mResources.getString(R.string.prefs_upload_location_local_value);
    }

    public String getPrinterStateClosedString() {
        return mResources.getString(R.string.printer_state_closed);
    }

    public String getAccountTypeString() {
        return mResources.getString(R.string.account_type);
    }
}
