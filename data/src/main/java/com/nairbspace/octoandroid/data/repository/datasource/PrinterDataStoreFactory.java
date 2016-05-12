package com.nairbspace.octoandroid.data.repository.datasource;

import com.nairbspace.octoandroid.data.disk.DiskManager;
import com.nairbspace.octoandroid.data.net.ApiManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrinterDataStoreFactory {

    private final DiskManager mDiskManager;
    private final ApiManager mApiManager;

    @Inject
    public PrinterDataStoreFactory(DiskManager diskManager, ApiManager apiManager) {
        mDiskManager = diskManager;
        mApiManager = apiManager;
    }

    public PrinterDataStore createDiskDataStore() {
        PrinterDataStore printerDataStore;
        printerDataStore = new DiskPrinterDataStore(mDiskManager, mApiManager);
        return printerDataStore;
    }

    public PrinterDataStore createCloudDataStore() {
        PrinterDataStore printerDataStore;
        printerDataStore = new CloudPrinterDataStore(mApiManager, mDiskManager);
        return printerDataStore;
    }

    public PrinterDataStore create() {
        PrinterDataStore printerDataStore;

        if (mDiskManager.isSaved() && !mDiskManager.isExpired()) {
            printerDataStore = createDiskDataStore();
        } else {
            printerDataStore = createCloudDataStore();
        }
        return printerDataStore;
    }
}
