package com.nairbspace.octoandroid.di.modules;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.nairbspace.octoandroid.data.db.DaoMaster;
import com.nairbspace.octoandroid.data.db.DaoSession;
import com.nairbspace.octoandroid.data.db.PrinterDbEntityDao;
import com.nairbspace.octoandroid.data.db.helper.DbOpenHelper;
import com.nairbspace.octoandroid.data.disk.PrefHelper;
import com.nairbspace.octoandroid.data.disk.ResManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {
    public static final String DB_NAME = "printer-db";

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    PrefHelper providePrefManager(SharedPreferences sp, ResManager res) {
        return new PrefHelper(sp, res);
    }

    @Provides
    @Singleton
    AccountManager provideAccountManager(Context context) {
        return AccountManager.get(context);
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
    PrinterDbEntityDao providePrinterDao(DaoSession daoSession) {
        return daoSession.getPrinterDbEntityDao();
    }
}

