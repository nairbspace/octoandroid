package com.nairbspace.octoandroid.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nairbspace.octoandroid.model.DaoMaster;

public class DbOpenHelper extends DaoMaster.OpenHelper {

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Currently Version 1
    }
}
