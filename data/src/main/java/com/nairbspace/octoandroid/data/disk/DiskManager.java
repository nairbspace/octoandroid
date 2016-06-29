package com.nairbspace.octoandroid.data.disk;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public interface DiskManager {

    boolean isDbEmpty();

    Observable.OnSubscribe<PrinterDbEntity> getPrinterInDb();

    Observable.OnSubscribe<List<PrinterDbEntity>> getPrintersInDb();

    Observable.OnSubscribe<PrinterDbEntity> getPrinterByName(String name);

    Observable.OnSubscribe<PrinterDbEntity> getPrinterById(long id);

    Func1<PrinterDbEntity, PrinterDbEntity> putPrinterInDb();

    Func1<PrinterDbEntity, PrinterDbEntity> putPrinterInPrefs();

    Func1<VersionEntity, VersionEntity> putVersionInDb();

    Action1<ConnectionEntity> putConnectionInDb();

    Func1<PrinterDbEntity, Boolean> syncDbAndAccountDeletion();

    Func1<PrinterDbEntity, Boolean> deletePrinterById();

    Action1<Throwable> deleteUnverifiedPrinter(long id);

    boolean isSaved();

    boolean isExpired();

    Func1<PrinterDbEntity, ConnectionEntity> getConnectionInDb();

    Observable<Boolean> isPushNotificationOn();

    Observable<Boolean> isStickyNotificationOn();

    long setActivePrinter(long id);

    long getActivePrinterId();

    PrinterDbEntity getPrinterByEditPrefId();

    Observable putEditPrinterDbEntityInDb();

    Action1<Throwable> deleteFailedEdit(PrinterDbEntity oldEntity, long activeId);

    Action0 resetActivePrinter(long activeId);
}