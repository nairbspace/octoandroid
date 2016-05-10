package com.nairbspace.octoandroid.data.disk;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
import com.nairbspace.octoandroid.data.exception.NoActivePrinterException;
import com.nairbspace.octoandroid.data.exception.PrinterDataNotFoundException;
import com.nairbspace.octoandroid.data.mapper.DataMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

@Singleton
public class DiskManagerImpl implements DiskManager {

    private final PrefHelper mPrefHelper;
    private final AccountHelper mAccountHelper;
    private final DbHelper mDbHelper;
    private final DataMapper mMapper;

    @Inject
    public DiskManagerImpl(PrefHelper prefHelper, AccountHelper accountHelper,
                           DbHelper dbHelper, DataMapper mapper) {
        mPrefHelper = prefHelper;
        mAccountHelper = accountHelper;
        mDbHelper = dbHelper;
        mMapper = mapper;
    }

    @Override
    public Observable<PrinterDbEntity> get() {
        return Observable.create(new Observable.OnSubscribe<PrinterDbEntity>() {
            @Override
            public void call(Subscriber<? super PrinterDbEntity> subscriber) {

                if (mPrefHelper.doesActivePrinterExist()) {
                    subscriber.onError(new NoActivePrinterException());
                }

                long printerId = mPrefHelper.getActivePrinter();
                PrinterDbEntity printerDbEntity = mDbHelper.getPrinterFromDbById(printerId);
                if (printerDbEntity == null) {
                    subscriber.onError(new PrinterDataNotFoundException());
                }

                if (!mAccountHelper.doesPrinterExistInAccountManager(printerDbEntity)) {
                    mAccountHelper.addAccount(printerDbEntity);
                }

                subscriber.onNext(printerDbEntity);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<PrinterDbEntity> get(final String name) {
        return Observable.create(new Observable.OnSubscribe<PrinterDbEntity>() {
            @Override
            public void call(Subscriber<? super PrinterDbEntity> subscriber) {
                PrinterDbEntity printerDbEntity = mDbHelper.getPrinterFromDbByName(name);
                if (printerDbEntity == null) {
                    subscriber.onError(new PrinterDataNotFoundException());
                }
                subscriber.onNext(printerDbEntity);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void put(PrinterDbEntity printerDbEntity, VersionEntity versionEntity) {
        String versionJson = mMapper.serialize(versionEntity);
        printerDbEntity.setVersionJson(versionJson);
        mDbHelper.deleteOldPrinterInDb(printerDbEntity);
        mDbHelper.addPrinterToDb(printerDbEntity);
        mPrefHelper.setActivePrinter(printerDbEntity.getId());
        mAccountHelper.addAccount(printerDbEntity);
    }

    @Override
    public Observable<Boolean> deleteOldPrinterInDbObservable(final PrinterDbEntity printerDbEntity) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                PrinterDbEntity oldPrinterDbEntity = mDbHelper
                        .getPrinterFromDbByName(printerDbEntity.getName());

                if (oldPrinterDbEntity == null) {
                    subscriber.onError(new PrinterDataNotFoundException());
                }

                if (mPrefHelper.isPrinterActive(oldPrinterDbEntity)) {
                    //TODO need to handle this when there's multiple printers!!
                    mPrefHelper.setActivePrinter(PrefHelper.NO_ACTIVE_PRINTER);
                }

                mDbHelper.deleteOldPrinterInDb(oldPrinterDbEntity);
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });
    }
}
