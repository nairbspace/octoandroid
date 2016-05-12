package com.nairbspace.octoandroid.data.repository.datasource;

import com.nairbspace.octoandroid.data.disk.DiskManager;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.net.ApiManager;

import rx.Observable;

public class DiskPrinterDataStore implements PrinterDataStore {

    private final DiskManager mDiskManager;
    private final ApiManager mApiManager;
    public DiskPrinterDataStore(DiskManager diskManager, ApiManager apiManager) {
        mDiskManager = diskManager;
        mApiManager = apiManager;
    }

    @Override
    public Observable<ConnectionEntity> connectionDetails() {
        return Observable.create(mDiskManager.getPrinterInDb())
                .map(mDiskManager.getConnectionInDb())
                .onExceptionResumeNext(mApiManager.getConnection())
                .concatMap(mApiManager.funcGetConnection())
                .doOnNext(mDiskManager.putConnectionInDb());
    }
}
