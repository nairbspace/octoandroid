package com.nairbspace.octoandroid.ui.add_printer;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class PlayServiceChecker {
    public static final int REQUEST_CODE = 1;
    private static final GoogleApiAvailability sGoogleApi = GoogleApiAvailability.getInstance();

    public static boolean isAvailable(Activity activity) {
        int result = sGoogleApi.isGooglePlayServicesAvailable(activity);
        if (result != ConnectionResult.SUCCESS) {
            if (sGoogleApi.isUserResolvableError(result)) {
                sGoogleApi.getErrorDialog(activity, result, REQUEST_CODE).show();
            }
            return false;
        }
        return true;
    }
}
