package com.nairbspace.octoandroid.data.repository.datasource;

import com.nairbspace.octoandroid.data.disk.DiskManager;
import com.nairbspace.octoandroid.data.net.ApiManagerImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrinterDataStoreFactory {

    private final DiskManager mDiskManager;
    private final ApiManagerImpl mApiManagerImpl;

    @Inject
    public PrinterDataStoreFactory(DiskManager diskManager, ApiManagerImpl apiManagerImpl) {
        mDiskManager = diskManager;
        mApiManagerImpl = apiManagerImpl;
    }

    public PrinterDataStore createDiskDataStore() {
        PrinterDataStore printerDataStore;
        printerDataStore = new DiskPrinterDataStore(mDiskManager);
        return printerDataStore;
    }

    public PrinterDataStore createCloudDataStore() {
        PrinterDataStore printerDataStore;
        printerDataStore = new CloudPrinterDataStore(mApiManagerImpl, mDiskManager);
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
