package com.nairbspace.octoandroid.data.repository.datasource;

import com.nairbspace.octoandroid.data.disk.DiskManager;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import rx.Observable;

public class DiskPrinterDataStore implements PrinterDataStore {

    private final DiskManager mDiskManager;

    public DiskPrinterDataStore(DiskManager diskManager) {
        mDiskManager = diskManager;
    }

    @Override
    public Observable<VersionEntity> printerVersionEntity() {
        return mDiskManager.getVersion();
    }

    @Override
    public Observable<ConnectionEntity> connectionEntityDetails() {
        return mDiskManager.getConnection();
    }
}
