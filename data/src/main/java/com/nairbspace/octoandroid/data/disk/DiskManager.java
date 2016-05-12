package com.nairbspace.octoandroid.data.disk;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public interface DiskManager {

    Observable.OnSubscribe<PrinterDbEntity> getPrinterInDb();

    Observable.OnSubscribe<PrinterDbEntity> getPrinterByName(String name);

    Action1<PrinterDbEntity> putPrinterInDb();

    Func1<VersionEntity, Boolean> putVersionInDb();

    Action1<ConnectionEntity> putConnectionInDb();

    Func1<PrinterDbEntity, Boolean> deletePrinterByName();

    Action1<Throwable> deleteUnverifiedPrinter();

    boolean isSaved();

    boolean isExpired();

    Func1<PrinterDbEntity, ConnectionEntity> getConnectionInDb();
}