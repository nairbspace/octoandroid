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

    public void resetActivePrinter() {
        setActivePrinter(mResManager.getNoActivePrinterValue());
    }

    public void setActivePrinter(long printerId) {
        mPreferences.edit().putLong(mResManager.getActivePrinterKey(), printerId).apply();
    }

    public long getActivePrinterId() {
        return mPreferences.getLong(mResManager.getActivePrinterKey(),
                mResManager.getNoActivePrinterValue());
    }

    public boolean isPrinterActive(long printerId) {
        return printerId == getActivePrinterId();
    }

    public boolean isPrinterActive(PrinterDbEntity printerDbEntity) {
        return printerDbEntity != null && isPrinterActive(printerDbEntity.getId());
    }

    public boolean doesActivePrinterExist() {
        return getActivePrinterId() != mResManager.getNoActivePrinterValue();
    }

    public void setSaveTimeMillis(long currentMillis) {
        mPreferences.edit().putLong(mResManager.getLastSaveTimeKey(), currentMillis).apply();
    }

    public long getLastSaveTimeMillis() {
        return mPreferences.getLong(mResManager.getLastSaveTimeKey(),
                mResManager.getDefaultSaveTimeValue());
    }

    public void setPrinterDbEntityToPrefs(PrinterDbEntity printerDbEntity) {
        mPreferences.edit().putLong(mResManager.getPrinterLongIdKey(), printerDbEntity.getId()).apply();
        mPreferences.edit().putString(mResManager.getPrinterNameKey(), printerDbEntity.getName()).apply();
        mPreferences.edit().putString(mResManager.getPrinterApiKey(), printerDbEntity.getApiKey()).apply();
        mPreferences.edit().putString(mResManager.getPrinterSchemeKey(), printerDbEntity.getScheme()).apply();
        mPreferences.edit().putString(mResManager.getPrinterHostKey(), printerDbEntity.getHost()).apply();

        // Have to store as string or else will have to override EditTextPreference and modify PreferenceFragment
        mPreferences.edit().putString(mResManager.getPrinterPortKey(), Integer.toString(printerDbEntity.getPort())).apply();

        mPreferences.edit().putString(mResManager.getWebsocketPathKey(), printerDbEntity.getWebsocketPath()).apply();
        mPreferences.edit().putString(mResManager.getWebcamPathQueryKey(), printerDbEntity.getWebcamPathQuery()).apply();
        mPreferences.edit().putString(mResManager.getPrinterUploadLocationKey(), printerDbEntity.getUploadLocation()).apply();
    }

    public PrinterDbEntity getEditPrinterDbEntity() {
        long id = mPreferences.getLong(mResManager.getPrinterLongIdKey(), mResManager.getNoActivePrinterValue());
        String name = mPreferences.getString(mResManager.getPrinterNameKey(), null);
        String apiKey = mPreferences.getString(mResManager.getPrinterApiKey(), null);
        String scheme = mPreferences.getString(mResManager.getPrinterSchemeKey(), null);
        String host = mPreferences.getString(mResManager.getPrinterHostKey(), null);

        int port = Integer.valueOf(mPreferences.getString(mResManager.getPrinterPortKey(), null));

        String websocketPath = mPreferences.getString(mResManager.getWebsocketPathKey(), null);
        String webcamPathQuery = mPreferences.getString(mResManager.getWebcamPathQueryKey(), null);
        String upload = mPreferences.getString(mResManager.getPrinterUploadLocationKey(),
                mResManager.getDefaultUploadLocationValue());

        PrinterDbEntity entity = new PrinterDbEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setApiKey(apiKey);
        entity.setScheme(scheme);
        entity.setHost(host);
        entity.setPort(port);
        entity.setWebsocketPath(websocketPath);
        entity.setWebcamPathQuery(webcamPathQuery);
        entity.setUploadLocation(upload);
        return entity;
    }

    public long getEditPrefsId() {
        return mPreferences.getLong(mResManager.getPrinterLongIdKey(), mResManager.getNoActivePrinterValue());
    }

    public boolean isPushNotificationOn() {
        return mPreferences.getBoolean(mResManager.getPushNotificationKey(), false);
    }

    public boolean isStickyNotificationOn() {
        return mPreferences.getBoolean(mResManager.getStickyNotificationKey(), false);
    }
}
