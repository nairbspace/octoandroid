package com.nairbspace.octoandroid.di.modules;

import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.nairbspace.octoandroid.data.db.DbOpenHelper;
import com.nairbspace.octoandroid.model.DaoMaster;
import com.nairbspace.octoandroid.model.DaoSession;
import com.nairbspace.octoandroid.model.PrinterDao;
import com.nairbspace.octoandroid.model.VersionDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {
    private static final String DB_NAME = "printer-db";

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    AccountManager provideAccountManager(Application application) {
        return AccountManager.get(application);
    }

    @Provides
    @Singleton
    DbOpenHelper provideDbOpenHelper(Context context) {
        return new DbOpenHelper(context, DB_NAME, null);
    }

    @Provides
    @Singleton
    SQLiteDatabase provideSqLiteDatabase(DbOpenHelper helper) {
        return helper.getWritableDatabase();
    }

    @Provides
    @Singleton
    DaoMaster provideDaoMaster(SQLiteDatabase db) {
        return new DaoMaster(db);
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession(DaoMaster daoMaster) {
        return daoMaster.newSession();
    }

    @Provides
    @Singleton
    PrinterDao providePrinterDao(DaoSession daoSession) {
        return daoSession.getPrinterDao();
    }

    @Provides
    @Singleton
    VersionDao provideVersionDao(DaoSession daoSession) {
        return daoSession.getVersionDao();
    }
}

