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

    public long getNoActivePrinterValue() {return (long) mResources.getInteger(R.integer.prefs_active_printer_none_value);}

    public String getActivePrinterKey() {return mResources.getString(R.string.prefs_active_printer_key);}

    public String getLastSaveTimeKey() {return mResources.getString(R.string.prefs_last_save_time_key);}

    public int getDefaultSaveTimeValue() {return mResources.getInteger(R.integer.prefs_last_save_time_default_value);}

    public String getDefaultUploadLocationValue() {return mResources.getString(R.string.prefs_printer_upload_location_local_value);}

    public String getPrinterStateClosedString() {return mResources.getString(R.string.printer_state_closed);}

    public String getAccountTypeString() {
        return mResources.getString(R.string.account_type);
    }

    public String getPrinterLongId() {return mResources.getString(R.string.prefs_printer_id_key);}

    public String getPrinterNameKey() {return mResources.getString(R.string.prefs_printer_name_key);}

    public String getPrinterHostKey() {return mResources.getString(R.string.prefs_printer_host_key);}

    public String getPrinterApiKey() {return mResources.getString(R.string.prefs_printer_api_key_key);}

    public String getPrinterSchemeKey() {return mResources.getString(R.string.prefs_printer_scheme_key);}

    public String getPrinterPortKey() {return mResources.getString(R.string.prefs_printer_port_key);}

    public String getWebsocketPathKey() {return mResources.getString(R.string.prefs_printer_websocket_path_key);}

    public String getWebcamPathQueryKey() {return mResources.getString(R.string.prefs_printer_webcam_path_query_key);}

    public String getPrinterUploadLocationKey() {return mResources.getString(R.string.prefs_printer_upload_location_key);}

    public String getPushNotificationKey() {return mResources.getString(R.string.prefs_push_notification_key);}

}
