package com.nairbspace.octoandroid.data.repository.datasource;

import com.nairbspace.octoandroid.data.disk.DiskManager;
import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.AddPrinterEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
import com.nairbspace.octoandroid.data.exception.PrinterDataNotFoundException;

import rx.Observable;

public class DiskPrinterDataStore implements PrinterDataStore {

    private final DiskManager mDiskManager;

    public DiskPrinterDataStore(DiskManager diskManager) {
        mDiskManager = diskManager;
    }

    @Override
    public Observable<PrinterDbEntity> printerDbEntityDetails() {
        return mDiskManager.get();
    }

    @Override
    public Observable<PrinterDbEntity> transformAddPrinterEntity(AddPrinterEntity addPrinterEntity) {
        // Can't add printer data until verified by cloud
        return Observable.error(new PrinterDataNotFoundException());
    }

    @Override
    public Observable<VersionEntity> printerVersionEntity(PrinterDbEntity printerDbEntity) {
        // Will only get printer version from cloud
        return Observable.error(new PrinterDataNotFoundException());
    }

    @Override
    public Observable<Boolean> deletePrinterDbEntityDetails(PrinterDbEntity printerDbEntity) {
        return mDiskManager.deleteOldPrinterInDbObservable(printerDbEntity);
    }

    @Override
    public Observable<PrinterDbEntity> printerDbEntityDetails(String name) {
        return mDiskManager.get(name);
    }
}
