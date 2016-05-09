package com.nairbspace.octoandroid.data.cache;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import rx.Observable;

public interface PrinterCache {

    Observable<PrinterDbEntity> get();

    void put(PrinterDbEntity printerDbEntity, VersionEntity versionEntity);
}