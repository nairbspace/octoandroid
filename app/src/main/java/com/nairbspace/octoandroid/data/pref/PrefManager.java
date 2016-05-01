package com.nairbspace.octoandroid.data.pref;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrefManager {
    private static final String ACTIVE_PRINTER = "active_printer";
    public static final long NO_ACTIVE_PRINTER = 0;

    private static PrefManager sPrefManager;
    private SharedPreferences mPreferences;

    @Inject
    public static PrefManager get(SharedPreferences sp) {
        if (sPrefManager == null) {
            sPrefManager = new PrefManager(sp);
        }
        return sPrefManager;
    }

    private PrefManager(SharedPreferences sp) {
        mPreferences = sp;
    }

    public void setActivePrinter(long printerId) {
        mPreferences.edit().putLong(ACTIVE_PRINTER, printerId).apply();
    }

    public long getActivePrinter() {
        return mPreferences.getLong(ACTIVE_PRINTER, NO_ACTIVE_PRINTER);
    }
}
