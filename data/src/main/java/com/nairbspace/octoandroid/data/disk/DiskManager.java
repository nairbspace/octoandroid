package com.nairbspace.octoandroid.data.disk;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import rx.Observable;

public interface DiskManager {

    Observable<PrinterDbEntity> get();

    Observable<PrinterDbEntity> get(String name);

    Observable<Boolean> putPrinterDbEntity(Observable<PrinterDbEntity> printerDbEntityObs);

    Observable<Boolean> putVersionEntity(Observable<VersionEntity> versionEntityObs);

    Observable<Boolean> deleteOldPrinterInDbObs(Observable<PrinterDbEntity> printerDbEntityObs);

    Observable<VersionEntity> getVersion();

    boolean isSaved();

    boolean isExpired();

    Observable<ConnectionEntity> getConnection();
}