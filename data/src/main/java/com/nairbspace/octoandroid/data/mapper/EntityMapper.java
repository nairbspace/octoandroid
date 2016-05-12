package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.domain.model.Printer;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

@Singleton
public class EntityMapper {

    private final PrinterDbEntityMapper mPrinterDbEntityMapper;
    private final ConnectionEntityMapper mConnectionEntityMapper;

    @Inject
    public EntityMapper(PrinterDbEntityMapper printerDbEntityMapper,
                        ConnectionEntityMapper connectionEntityMapper) {
        mPrinterDbEntityMapper = printerDbEntityMapper;
        mConnectionEntityMapper = connectionEntityMapper;
    }

    public Func1<PrinterDbEntity, Printer> maptoPrinter() {
        return mPrinterDbEntityMapper.maptoPrinter();
    }

    public Observable.OnSubscribe<PrinterDbEntity> mapAddPrinterToPrinterDbEntity(final AddPrinter addPrinter) {
        return mPrinterDbEntityMapper.mapAddPrinterToPrinterDbEntity(addPrinter);
    }

    public Func1<ConnectionEntity, Connection> mapToConnection() {
        return mConnectionEntityMapper.mapToConnection();
    }
}
