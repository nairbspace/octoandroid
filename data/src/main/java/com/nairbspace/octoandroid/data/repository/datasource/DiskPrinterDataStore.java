package com.nairbspace.octoandroid.data.repository.datasource;

import com.nairbspace.octoandroid.data.cache.PrinterCache;
import com.nairbspace.octoandroid.data.db.PrinterDbEntity;

import rx.Observable;

public class DiskPrinterDataStore implements PrinterDataStore {

    private final PrinterCache mPrinterCache;

    public DiskPrinterDataStore(PrinterCache printerCache) {
        mPrinterCache = printerCache;
    }

    @Override
    public Observable<PrinterDbEntity> printerEntityDetails() {
        return mPrinterCache.get();
    }
}
