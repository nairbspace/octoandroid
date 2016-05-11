package com.nairbspace.octoandroid.data.repository;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.disk.DiskManager;
import com.nairbspace.octoandroid.data.entity.AddPrinterEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
import com.nairbspace.octoandroid.data.mapper.EntityMapper;
import com.nairbspace.octoandroid.data.net.ApiManager;
import com.nairbspace.octoandroid.data.repository.datasource.PrinterDataStore;
import com.nairbspace.octoandroid.data.repository.datasource.PrinterDataStoreFactory;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@SuppressWarnings("UnnecessaryLocalVariable")
@Singleton
public class PrinterDataRepository implements PrinterRepository {

    private final PrinterDataStoreFactory mPrinterDataStoreFactory;
    private final EntityMapper mEntityMapper;
    private final DiskManager mDiskManager;
    private final ApiManager mApiManager;

    @Inject
    public PrinterDataRepository(EntityMapper entityMapper,
                                 PrinterDataStoreFactory printerDataStoreFactory,
                                 DiskManager diskManager, ApiManager apiManager) {
        mEntityMapper = entityMapper;
        mPrinterDataStoreFactory = printerDataStoreFactory;
        mDiskManager = diskManager;
        mApiManager = apiManager;
    }

    @Override
    public Observable<Printer> printerDetails() {
        Observable<PrinterDbEntity> printerDbEntityObs = mDiskManager.get();
        Observable<Printer> printerObs = mEntityMapper.mapPrinterDbEntityObs(printerDbEntityObs);
        return printerObs;
    }

    @Override
    public Observable<Boolean> addPrinterDetails(final AddPrinter addPrinter) {
        Observable<AddPrinterEntity> addPrinterEntityObs = mEntityMapper.mapAddPrinterToEntityObs(addPrinter);
        Observable<PrinterDbEntity> printerDbEntityObs = mApiManager.map(addPrinterEntityObs)
                .doOnNext(mDiskManager.putPrinterDbEntity());

        Observable<VersionEntity> versionEntityObs = printerDbEntityObs
                .concatMap(mApiManager.concatGetVersion())
                .doOnError(mDiskManager.deleteUnverifiedPrinter());

        Observable<Boolean> booleanObs = mDiskManager.putVersionEntity(versionEntityObs);

        return booleanObs;
    }

    @Override
    public Observable<Boolean> deletePrinterDetails(Printer printer) {
        Observable<PrinterDbEntity> printerDbEntityObs = mEntityMapper.mapPrinterToEntityObs(printer);
        Observable<Boolean> booleanObs = mDiskManager.deleteOldPrinterInDbObs(printerDbEntityObs);
        return booleanObs;
    }

    @Override
    public Observable<Printer> printerDetails(String name) {
        Observable<PrinterDbEntity> printerDbEntityObs = mDiskManager.get(name);
        Observable<Printer> printerObs = mEntityMapper.mapPrinterDbEntityObs(printerDbEntityObs);
        return printerObs;
    }

    @Override
    public Observable<Connection> connectionDetails() {
        PrinterDataStore printerDataStore = mPrinterDataStoreFactory.createCloudDataStore();
        Observable<ConnectionEntity> connectionEntityObs = printerDataStore.connectionEntityDetails();
        Observable<Connection> connectionObs = mEntityMapper.mapConnectionEntityObs(connectionEntityObs);
        return connectionObs;
    }
}
