package com.nairbspace.octoandroid.data.repository;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nairbspace.octoandroid.data.disk.DiskManager;
import com.nairbspace.octoandroid.data.mapper.MapperHelper;
import com.nairbspace.octoandroid.data.net.ApiManager;
import com.nairbspace.octoandroid.data.net.WebsocketManager;
import com.nairbspace.octoandroid.data.repository.datasource.PrinterDataStoreFactory;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.domain.model.FileCommand;
import com.nairbspace.octoandroid.domain.model.Files;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.domain.model.Websocket;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class PrinterDataRepository implements PrinterRepository {

    private final PrinterDataStoreFactory mPrinterDataStoreFactory;
    private final MapperHelper mMapperHelper;
    private final DiskManager mDiskManager;
    private final ApiManager mApiManager;
    private final WebsocketManager mWebsocket;

    @Inject
    public PrinterDataRepository(MapperHelper mapperHelper,
                                 PrinterDataStoreFactory printerDataStoreFactory,
                                 DiskManager diskManager, ApiManager apiManager,
                                 WebsocketManager websocket) {
        mMapperHelper = mapperHelper;
        mPrinterDataStoreFactory = printerDataStoreFactory;
        mDiskManager = diskManager;
        mApiManager = apiManager;
        mWebsocket = websocket;
    }

    @Override
    public Observable<Printer> printerDetails() {
        return Observable.create(mDiskManager.getPrinterInDb())
                .map(mMapperHelper.maptoPrinter());
    }

    @RxLogObservable
    @Override
    public Observable<Boolean> addPrinterDetails(final AddPrinter addPrinter) {
        return Observable.create(mMapperHelper.mapAddPrinterToPrinterDbEntity(addPrinter))
                .doOnNext(mDiskManager.putPrinterInDb())
                .concatMap(mApiManager.funcGetVersion())
                .doOnError(mDiskManager.deleteUnverifiedPrinter())
                .map(mDiskManager.putVersionInDb());
    }

    @Override
    public Observable<Boolean> deletePrinterByName(String name) {
        return Observable.create(mDiskManager.getPrinterByName(name))
                .map(mDiskManager.deletePrinterByName());
    }

    @RxLogObservable
    @Override
    public Observable<Connection> connectionDetails() {
        return mPrinterDataStoreFactory.create()
                .connectionDetails()
                .map(mMapperHelper.mapToConnection());
    }

    @Override
    public Observable connectToPrinter(Connect connect) {
        return Observable.create(mMapperHelper.mapToConnectEntity(connect))
                .flatMap(mApiManager.connectToPrinter());
    }

    @Override
    public Observable<Files> getAllFiles() {
        return mApiManager.getAllFiles().map(mMapperHelper.mapToFiles());
    }

    @Override
    public Observable<Websocket> getWebsocket() {
        return mWebsocket.getWebsocketObservable()
                .map(mMapperHelper.mapToWebsocket());
    }

    @Override
    public Observable sendJobCommand(HashMap<String, String> command) {
        return mApiManager.sendJobCommand(command);
    }

    @Override
    public Observable sendFileCommand(final FileCommand fileCommand) {
        final String apiUrl = fileCommand.apiUrl();
        return Observable.create(mMapperHelper.mapToFileCommandEntity(fileCommand))
                .concatMap(mApiManager.funcStartFilePrint(apiUrl));
    }

    @Override
    public Observable deleteFile(String url) {
        return mApiManager.deleteFile(url);
    }
}
