package com.nairbspace.octoandroid.data.repository.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nairbspace.octoandroid.data.cache.PrinterCache;

public class PrinterDataStoreFactory {

    private final Context mContext;
    private final PrinterCache mPrinterCache;

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
