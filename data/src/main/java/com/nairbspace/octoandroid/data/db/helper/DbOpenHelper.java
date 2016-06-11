package com.nairbspace.octoandroid.data.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nairbspace.octoandroid.data.db.DaoMaster;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DbOpenHelper extends DaoMaster.OpenHelper {

    @Inject
    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) Version2.update(db);
//         if(oldVersion < 3) db.execSQL(UpgradeToVersion3.STATEMENT);
    }
}
