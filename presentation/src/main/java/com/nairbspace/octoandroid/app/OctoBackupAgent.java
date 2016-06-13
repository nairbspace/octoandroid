package com.nairbspace.octoandroid.app;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;

import com.nairbspace.octoandroid.di.modules.StorageModule;

import java.io.File;

public class OctoBackupAgent extends BackupAgentHelper {
    private static final String DEFAULT_PREFS_FILENAME = "com.nairbspace.octoandroid_preferences";
    private static final String DEFAULT_PREFS_KEY = "default_prefs_key";

    private static final String PRINTER_DB_KEY = "db_key";

    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper prefBackupHelper = new SharedPreferencesBackupHelper(this, DEFAULT_PREFS_FILENAME);
        addHelper(DEFAULT_PREFS_KEY, prefBackupHelper);

        // TODO not sure if need to implement synchronized since GreenDAO is thread safe...
        FileBackupHelper dbBackupHelper = new FileBackupHelper(this, StorageModule.DB_NAME);
        addHelper(PRINTER_DB_KEY, dbBackupHelper);
    }

    // Will not be able to backup files in the files path by overriding this for future reference.
    @Override
    public File getFilesDir() {
        File path = getDatabasePath(StorageModule.DB_NAME);
        return path.getParentFile();
    }
}
