package com.nairbspace.octoandroid.data.repository.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nairbspace.octoandroid.data.cache.PrinterCache;
import com.nairbspace.octoandroid.data.net.ApiConnection;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrinterDataStoreFactory {

    private final Context mContext;
    private final PrinterCache mPrinterCache;
    private final ApiConnection mApiConnection;

    @Inject
    public PrinterDataStoreFactory(@NonNull Context context, @NonNull PrinterCache printerCache,
                                   @NonNull ApiConnection apiConnection) {
        mContext = context;
        mPrinterCache = printerCache;
        mApiConnection = apiConnection;
    }

    public PrinterDataStore create() {
        PrinterDataStore printerDataStore;
        printerDataStore = new DiskPrinterDataStore(mPrinterCache);
        return printerDataStore;
    }

    public PrinterDataStore createCloudDataStore() {
        PrinterDataStore printerDataStore;
        printerDataStore = new CloudPrinterDataStore(mApiConnection, mPrinterCache);
        return printerDataStore;
    }
}
