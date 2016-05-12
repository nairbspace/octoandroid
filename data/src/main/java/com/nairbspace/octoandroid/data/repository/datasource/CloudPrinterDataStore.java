package com.nairbspace.octoandroid.data.repository.datasource;

import com.nairbspace.octoandroid.data.disk.DiskManager;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.net.ApiManager;

import rx.Observable;

public class CloudPrinterDataStore implements PrinterDataStore {

    private final ApiManager mApiManager;
    private final DiskManager mDiskManager;

    public CloudPrinterDataStore(ApiManager apiManager, DiskManager diskManager) {
        mApiManager = apiManager;
        mDiskManager = diskManager;
    }

    @Override
    public Observable<ConnectionEntity> connectionDetails() {
        return mApiManager.getConnection()
                .doOnNext(mDiskManager.putConnectionInDb());
    }
}
