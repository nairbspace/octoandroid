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
        return mPreferences.getString(mResManager.getPrinterUploadLocationKey(),
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

    public void setPrinterDbEntityToPrefs(PrinterDbEntity printerDbEntity) {
        mPreferences.edit().putLong(mResManager.getPrinterLongId(), printerDbEntity.getId()).apply();
        mPreferences.edit().putString(mResManager.getPrinterNameKey(), printerDbEntity.getName()).apply();
        mPreferences.edit().putString(mResManager.getPrinterApiKey(), printerDbEntity.getApiKey()).apply();
        mPreferences.edit().putString(mResManager.getPrinterApiKey(), printerDbEntity.getApiKey()).apply();
        mPreferences.edit().putString(mResManager.getPrinterSchemeKey(), printerDbEntity.getScheme()).apply();
        mPreferences.edit().putString(mResManager.getPrinterHostKey(), printerDbEntity.getHost()).apply();

        // Have to store as string or else will have to override EditTextPreference and modify PreferenceFragment
        mPreferences.edit().putString(mResManager.getPrinterPortKey(), Integer.toString(printerDbEntity.getPort())).apply();

        mPreferences.edit().putString(mResManager.getWebsocketPathKey(), printerDbEntity.getWebsocketPath()).apply();
        mPreferences.edit().putString(mResManager.getWebcamPathQueryKey(), printerDbEntity.getWebcamPathQuery()).apply();
        mPreferences.edit().putString(mResManager.getPrinterUploadLocationKey(), printerDbEntity.getUploadLocation()).apply();
    }

    public boolean isPushNotificationOn() {
        return mPreferences.getBoolean(mResManager.getPushNotificationKey(), false);
    }
}
