package com.nairbspace.octoandroid.data.disk;

import android.content.SharedPreferences;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

/** Convenience methods for SharedPreferences related info */
@Singleton
public class PrefHelper {
    private static final String ACTIVE_PRINTER = "active_printer";
    public static final long NO_ACTIVE_PRINTER = 0;
    private static final String LAST_SAVE_TIME_KEY = "last_save_time";

    private SharedPreferences mPreferences;

    @Inject
    public PrefHelper(SharedPreferences sp) {
        mPreferences = sp;
    }

    public void setActivePrinter(long printerId) {
        mPreferences.edit().putLong(ACTIVE_PRINTER, printerId).apply();
    }

    public long getActivePrinter() {
        return mPreferences.getLong(ACTIVE_PRINTER, NO_ACTIVE_PRINTER);
    }

    public boolean isPrinterActive(long printerId) {
        return printerId == getActivePrinter();
    }

    public boolean isPrinterActive(PrinterDbEntity printerDbEntity) {
        return printerDbEntity != null &&
                printerDbEntity.getId() != null &&
                isPrinterActive(printerDbEntity.getId());
    }

    public boolean doesActivePrinterExist() {
        return getActivePrinter() != NO_ACTIVE_PRINTER;
    }

    public void setSaveTimeMillis(long currentMillis) {
        mPreferences.edit().putLong(LAST_SAVE_TIME_KEY, currentMillis).apply();
    }

    public long getLastSaveTimeMillis() {
        return mPreferences.getLong(LAST_SAVE_TIME_KEY, 0);
    }
}
