package com.nairbspace.octoandroid.data.disk;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import rx.Observable;
import rx.functions.Action1;

public interface DiskManager {

    Observable<PrinterDbEntity> get();

    Observable<PrinterDbEntity> get(String name);

    Action1<PrinterDbEntity> putPrinterDbEntity();

    Observable<Boolean> putVersionEntity(Observable<VersionEntity> versionEntityObs);

    Observable<Boolean> deleteOldPrinterInDbObs(Observable<PrinterDbEntity> printerDbEntityObs);

    Action1<Throwable> deleteUnverifiedPrinter();

    Observable<VersionEntity> getVersion();

    boolean isSaved();

    boolean isExpired();

    Observable<ConnectionEntity> getConnection();
}