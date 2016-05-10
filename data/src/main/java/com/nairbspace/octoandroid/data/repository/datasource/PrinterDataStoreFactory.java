package com.nairbspace.octoandroid.data.repository.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nairbspace.octoandroid.data.disk.DiskManager;
import com.nairbspace.octoandroid.data.net.ApiConnection;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrinterDataStoreFactory {

    private final Context mContext;
    private final DiskManager mDiskManager;
    private final ApiConnection mApiConnection;

    @Inject
    public PrinterDataStoreFactory(@NonNull Context context, @NonNull DiskManager diskManager,
                                   @NonNull ApiConnection apiConnection) {
        mContext = context;
        mDiskManager = diskManager;
        mApiConnection = apiConnection;
    }

    public PrinterDataStore createDiskDataStore() {
        PrinterDataStore printerDataStore;
        printerDataStore = new DiskPrinterDataStore(mDiskManager);
        return printerDataStore;
    }

    public PrinterDataStore createCloudDataStore() {
        PrinterDataStore printerDataStore;
        printerDataStore = new CloudPrinterDataStore(mApiConnection, mDiskManager);
        return printerDataStore;
    }
}
