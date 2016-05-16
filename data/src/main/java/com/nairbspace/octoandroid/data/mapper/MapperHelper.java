package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.ConnectEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.FilesEntity;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.domain.model.Files;
import com.nairbspace.octoandroid.domain.model.Printer;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

@Singleton
public class MapperHelper {

    private final PrinterDbEntityMapper mPrinterDbEntityMapper;
    private final ConnectionEntityMapper mConnectionEntityMapper;
    private final ConnectEntityMapper mConnectEntityMapper;
    private final FilesEntityMapper mFilesEntityMapper;

    @Inject
    public MapperHelper(PrinterDbEntityMapper printerDbEntityMapper,
                        ConnectionEntityMapper connectionEntityMapper,
                        ConnectEntityMapper connectEntityMapper,
                        FilesEntityMapper filesEntityMapper) {
        mPrinterDbEntityMapper = printerDbEntityMapper;
        mConnectionEntityMapper = connectionEntityMapper;
        mConnectEntityMapper = connectEntityMapper;
        mFilesEntityMapper = filesEntityMapper;
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

    public Observable.OnSubscribe<ConnectEntity> mapToConnectEntity(final Connect connect) {
        return mConnectEntityMapper.mapToConnectEntity(connect);
    }

    public Func1<FilesEntity, Files> mapToFiles() {
        return mFilesEntityMapper.mapToFiles();
    }
}
