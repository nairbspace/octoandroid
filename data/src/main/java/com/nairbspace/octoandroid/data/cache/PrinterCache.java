package com.nairbspace.octoandroid.data.cache;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import rx.Observable;

public interface PrinterCache {

    Observable<PrinterDbEntity> get();

    Observable<PrinterDbEntity> get(String name);

    void put(PrinterDbEntity printerDbEntity, VersionEntity versionEntity);

    Observable<Boolean> deleteOldPrinterInDbObservable(PrinterDbEntity printerDbEntity);
}