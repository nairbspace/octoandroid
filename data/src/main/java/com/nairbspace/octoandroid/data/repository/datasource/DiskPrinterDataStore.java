package com.nairbspace.octoandroid.data.repository.datasource;

import com.nairbspace.octoandroid.data.cache.PrinterCache;
import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.AddPrinterEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
import com.nairbspace.octoandroid.data.exception.PrinterDataNotFoundException;

import rx.Observable;

public class DiskPrinterDataStore implements PrinterDataStore {

    private final PrinterCache mPrinterCache;

    public DiskPrinterDataStore(PrinterCache printerCache) {
        mPrinterCache = printerCache;
    }

    @Override
    public Observable<PrinterDbEntity> printerDbEntityDetails() {
        return mPrinterCache.get();
    }

    @Override
    public Observable<PrinterDbEntity> transformAddPrinterEntity(AddPrinterEntity addPrinterEntity) {
        // Can't add printer data until verified by cloud
        return Observable.error(new PrinterDataNotFoundException());
    }

    @Override
    public Observable<VersionEntity> printerVersion(PrinterDbEntity printerDbEntity) {
        // Will only get printer version from cloud
        return Observable.error(new PrinterDataNotFoundException());
    }
}
