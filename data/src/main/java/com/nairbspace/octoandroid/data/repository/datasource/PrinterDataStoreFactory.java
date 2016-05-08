package com.nairbspace.octoandroid.data.repository.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nairbspace.octoandroid.data.cache.PrinterCache;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrinterDataStoreFactory {

    @SuppressWarnings("FieldCanBeLocal")
    private final Context mContext;
    private final PrinterCache mPrinterCache;

    @Inject
    public PrinterDataStoreFactory(@NonNull Context context, @NonNull PrinterCache printerCache) {
        mContext = context;
        mPrinterCache = printerCache;
    }

    public PrinterDataStore create() {
        PrinterDataStore printerDataStore;
        printerDataStore = new DiskPrinterDataStore(mPrinterCache);
        return printerDataStore;
    }
}
