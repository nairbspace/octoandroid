package com.nairbspace.octoandroid.data.repository.datasource;

import com.nairbspace.octoandroid.data.cache.PrinterCache;
import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.AddPrinterEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
import com.nairbspace.octoandroid.data.exception.PrinterDataNotFoundException;
import com.nairbspace.octoandroid.data.net.ApiConnection;

import rx.Observable;

public class CloudPrinterDataStore implements PrinterDataStore {

    private final ApiConnection mApiConnection;
    private final PrinterCache mPrinterCache;

    public CloudPrinterDataStore(ApiConnection apiConnection, PrinterCache printerCache) {
        mApiConnection = apiConnection;
        mPrinterCache = printerCache;
    }

    @Override
    public Observable<PrinterDbEntity> printerDbEntityDetails() {
        // Can't fetch database from cloud...
        return Observable.error(new PrinterDataNotFoundException());
    }

    @Override
    public Observable<PrinterDbEntity> transformAddPrinterEntity(AddPrinterEntity addPrinterEntity) {
        return mApiConnection.transformAddPrinterEntity(addPrinterEntity);
    }

    @Override
    public Observable<VersionEntity> printerVersionEntity(PrinterDbEntity printerDbEntity) {
        mApiConnection.setOctoInterceptor(printerDbEntity);
        return mApiConnection.getVersion();
    }

    @Override
    public Observable<Boolean> deletePrinterDbEntityDetails(PrinterDbEntity printerDbEntity) {
        // Can't delete printer db from cloud
        return Observable.error(new PrinterDataNotFoundException());
    }

    @Override
    public Observable<PrinterDbEntity> printerDbEntityDetails(String name) {
        return Observable.error(new PrinterDataNotFoundException());
    }
}
