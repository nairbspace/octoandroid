package com.nairbspace.octoandroid.data.disk;

import android.content.SharedPreferences;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

/** Convenience methods for SharedPreferences related info */
@Singleton
public class PrefHelper {
    private final SharedPreferences mPreferences;
    private final ResManager mResManager;

    @Inject
    public PrefHelper(SharedPreferences sp, ResManager resManager) {
        mPreferences = sp;
        mResManager = resManager;
    }

    public String getUploadLocation() {
        return mPreferences.getString(mResManager.getUploadLocationKey(),
                mResManager.getDefaultUploadLocationValue());
    }

    public void setActivePrinter(long printerId) {
        mPreferences.edit().putLong(mResManager.getActivePrinterKey(), printerId).apply();
    }

    public long getActivePrinter() {
        return mPreferences.getLong(mResManager.getActivePrinterKey(),
                mResManager.getNoActivePrinterValue());
    }

    public boolean isPrinterActive(long printerId) {
        return printerId == getActivePrinter();
    }

    public boolean isPrinterActive(PrinterDbEntity printerDbEntity) {
        return printerDbEntity != null && isPrinterActive(printerDbEntity.getId());
    }

    public boolean doesActivePrinterExist() {
        return getActivePrinter() != mResManager.getNoActivePrinterValue();
    }

    public void setSaveTimeMillis(long currentMillis) {
        mPreferences.edit().putLong(mResManager.getLastSaveTimeKey(), currentMillis).apply();
    }

    public long getLastSaveTimeMillis() {
        return mPreferences.getLong(mResManager.getLastSaveTimeKey(),
                mResManager.getDefaultSaveTimeValue());
    }
}
