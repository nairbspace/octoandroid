package com.nairbspace.octoandroid.data.repository;

import com.nairbspace.octoandroid.data.disk.DiskManager;
import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.AddPrinterEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
import com.nairbspace.octoandroid.data.mapper.DataMapper;
import com.nairbspace.octoandroid.data.repository.datasource.PrinterDataStore;
import com.nairbspace.octoandroid.data.repository.datasource.PrinterDataStoreFactory;
import com.nairbspace.octoandroid.domain.AddPrinter;
import com.nairbspace.octoandroid.domain.Printer;
import com.nairbspace.octoandroid.domain.Version;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

@Singleton
public class PrinterDataRepository implements PrinterRepository {

    private final PrinterDataStoreFactory mPrinterDataStoreFactory;
    private final DataMapper mDataMapper;
    private final DiskManager mDiskManager;
    private PrinterDbEntity mPrinterDbEntity;

    @Inject
    public PrinterDataRepository(DataMapper dataMapper,
                                 PrinterDataStoreFactory printerDataStoreFactory,
                                 DiskManager diskManager) {
        mDataMapper = dataMapper;
        mPrinterDataStoreFactory = printerDataStoreFactory;
        mDiskManager = diskManager;
    }

    @Override
    public Observable<Printer> printerDetails() {
        final PrinterDataStore printerDataStore = mPrinterDataStoreFactory.createDiskDataStore();
        return printerDataStore.printerDbEntityDetails()
                .map(new Func1<PrinterDbEntity, Printer>() {
                    @Override
                    public Printer call(PrinterDbEntity printerDbEntity) {
                        return mDataMapper.transformWithNoId(printerDbEntity);
                    }
                });
    }

    @Override
    public Observable<Printer> transformAddPrinter(final AddPrinter addPrinter) {
        return Observable.create(new Observable.OnSubscribe<AddPrinterEntity>() {
            @Override
            public void call(Subscriber<? super AddPrinterEntity> subscriber) {
                try {
                    AddPrinterEntity addPrinterEntity = mDataMapper.transform(addPrinter);
                    subscriber.onNext(addPrinterEntity);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).flatMap(new Func1<AddPrinterEntity, Observable<PrinterDbEntity>>() {
            @Override
            public Observable<PrinterDbEntity> call(AddPrinterEntity addPrinterEntity) {
                PrinterDataStore printerDataStore = mPrinterDataStoreFactory.createCloudDataStore();
                return printerDataStore.transformAddPrinterEntity(addPrinterEntity);
            }
        }).map(new Func1<PrinterDbEntity, Printer>() {
            @Override
            public Printer call(PrinterDbEntity printerDbEntity) {
                return mDataMapper.transformWithNoId(printerDbEntity);
            }
        });
    }

    @Override
    public Observable<Version> printerVersion(final Printer printer) {
        return Observable.create(new Observable.OnSubscribe<PrinterDbEntity>() {
            @Override
            public void call(Subscriber<? super PrinterDbEntity> subscriber) {
                try {
                    PrinterDbEntity printerDbEntity = mDataMapper.transformToEntity(printer);
                    subscriber.onNext(printerDbEntity);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).flatMap(new Func1<PrinterDbEntity, Observable<VersionEntity>>() {
            @Override
            public Observable<VersionEntity> call(PrinterDbEntity printerDbEntity) {
                mPrinterDbEntity = printerDbEntity;
                PrinterDataStore printerDataStore = mPrinterDataStoreFactory.createCloudDataStore();
                return printerDataStore.printerVersionEntity(printerDbEntity);
            }
        }).map(new Func1<VersionEntity, Version>() {
            @Override
            public Version call(VersionEntity versionEntity) {
                mDiskManager.put(mPrinterDbEntity, versionEntity);
                return mDataMapper.transform(versionEntity);
            }
        });
    }

    @Override
    public Observable<Boolean> deletePrinterDetails(final Printer printer) {
        return Observable.create(new Observable.OnSubscribe<PrinterDbEntity>() {
            @Override
            public void call(Subscriber<? super PrinterDbEntity> subscriber) {
                try {
                    PrinterDbEntity printerDbEntity = mDataMapper.transformToEntity(printer);
                    subscriber.onNext(printerDbEntity);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).flatMap(new Func1<PrinterDbEntity, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(PrinterDbEntity printerDbEntity) {
                PrinterDataStore printerDataStore = mPrinterDataStoreFactory.createDiskDataStore();
                return printerDataStore.deletePrinterDbEntityDetails(printerDbEntity);
            }
        });
    }

    @Override
    public Observable<Printer> printerDetails(String name) {
        PrinterDataStore printerDataStore = mPrinterDataStoreFactory.createDiskDataStore();
        return printerDataStore.printerDbEntityDetails(name)
                .map(new Func1<PrinterDbEntity, Printer>() {
                    @Override
                    public Printer call(PrinterDbEntity printerDbEntity) {
                        return mDataMapper.transformWithNoId(printerDbEntity);
                    }
                });
    }
}
