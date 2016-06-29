package com.nairbspace.octoandroid.data.repository;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.disk.DiskManager;
import com.nairbspace.octoandroid.data.mapper.MapperHelper;
import com.nairbspace.octoandroid.data.net.ApiManager;
import com.nairbspace.octoandroid.data.net.WebsocketManager;
import com.nairbspace.octoandroid.data.net.stream.WebcamManager;
import com.nairbspace.octoandroid.data.repository.datasource.PrinterDataStoreFactory;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.domain.model.ArbitraryCommand;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.domain.model.FileCommand;
import com.nairbspace.octoandroid.domain.model.Files;
import com.nairbspace.octoandroid.domain.model.PrintHeadCommand;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.domain.model.Slicer;
import com.nairbspace.octoandroid.domain.model.SlicingCommand;
import com.nairbspace.octoandroid.domain.model.TempCommand;
import com.nairbspace.octoandroid.domain.model.ToolCommand;
import com.nairbspace.octoandroid.domain.model.Websocket;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final WebcamManager mWebcamManager;

    @Inject
    public PrinterDataRepository(MapperHelper mapperHelper,
                                 PrinterDataStoreFactory printerDataStoreFactory,
                                 DiskManager diskManager, ApiManager apiManager,
                                 WebsocketManager websocket, WebcamManager webcamManager) {
        mMapperHelper = mapperHelper;
        mPrinterDataStoreFactory = printerDataStoreFactory;
        mDiskManager = diskManager;
        mApiManager = apiManager;
        mWebsocket = websocket;
        mWebcamManager = webcamManager;
    }

    @Override
    public Observable<Boolean> isDbEmpty() {
        return Observable.just(mDiskManager.isDbEmpty());
    }

    @Override
    public Observable<Printer> printerDetails() {
        return Observable.create(mDiskManager.getPrinterInDb())
                .map(mMapperHelper.maptoPrinter());
    }

    @Override
    public Observable<List<Printer>> getPrinters() {
        return Observable.create(mDiskManager.getPrintersInDb())
                .map(mMapperHelper.mapToPrinters());
    }

    @Override
    public Observable addPrinterDetails(AddPrinter addPrinter) {
        // Stash active printer in case adding new printer fails to reset.
        long id = mDiskManager.getActivePrinterId();

        Observable entityObs = Observable
                .create(mMapperHelper.mapAddPrinterToPrinterDbEntity(addPrinter))
                .map(mDiskManager.putPrinterInDb());

        Observable verifyObs = mApiManager.getVersion()
                .map(mDiskManager.putVersionInDb());

        return Observable.concat(entityObs, verifyObs)
                .doOnError(mDiskManager.deleteUnverifiedPrinter(id));
    }

    @Override
    public Observable setPrinterPrefs(long id) {
        return Observable.create(mDiskManager.getPrinterById(id))
                .map(mDiskManager.putPrinterInPrefs());
    }

    @Override
    public Observable<Boolean> syncDbAndAccountDeletion(String name) {
        return Observable.create(mDiskManager.getPrinterByName(name))
                .map(mDiskManager.syncDbAndAccountDeletion());
    }

    @Override
    public Observable<Boolean> deletePrinterById(long id) {
        return Observable.create(mDiskManager.getPrinterById(id))
                .map(mDiskManager.deletePrinterById());
    }

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
                .map(mMapperHelper.mapToWebsocket())
                .retry();
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

    @Override
    public Observable uploadFile(String uriString) {
        return Observable.create(mMapperHelper.mapToMultiPartBodyPart(uriString))
                .concatMap(mApiManager.funcUploadFile());
    }

    @Override
    public Observable connectToWebcam() {
        return mWebcamManager.connect();
    }

    @Override
    public Observable sendTempCommand(final TempCommand tempCommand) {
        return Observable.create(mMapperHelper.mapToTempCommandEntity(tempCommand))
                .concatMap(mApiManager.funcSendToolOrBedCommand(tempCommand));
    }

    @Override
    public Observable sendPrintHeadCommand(PrintHeadCommand command) {
        return Observable.create(mMapperHelper.mapToPrintHeadCommandEntity(command))
                .concatMap(mApiManager.funcSendPrintHeadCommand());
    }

    @Override
    public Observable selectTool(int tool) {
        return mApiManager.selectTool(mMapperHelper.mapToToolCommandEntitySelect(tool));
    }

    @Override
    public Observable sendToolCommand(ToolCommand toolCommand) {
        return Observable.create(mMapperHelper.mapToToolCommandEntity(toolCommand))
                .concatMap(mApiManager.funcSendToolCommand());

    }

    @Override
    public Observable sendArbitraryCommand(ArbitraryCommand arbitraryCommand) {
        return mApiManager.sendArbitraryCommand(mMapperHelper.mapToArbitraryCommandEntity(arbitraryCommand));
    }

    @Override
    public Observable<Boolean> isPushNotificationOn() {
        return mDiskManager.isPushNotificationOn();
    }

    @Override
    public Observable<Boolean> isStickyNotificationOn() {
        return mDiskManager.isStickyNotificationOn();
    }

    @Override
    public Observable<Map<String, Slicer>> getSlicers() {
        return mApiManager.getSlicers().map(mMapperHelper.mapToSlicer());
    }

    @Override
    public Observable sendSliceCommand(SlicingCommand slicingCommand) {
        String url = slicingCommand.apiUrl();
        return Observable.create(mMapperHelper.mapToSlicingCommandEntity(slicingCommand))
                .concatMap(mApiManager.funcSendSliceCommand(url));
    }

    @Override
    public Observable setActivePrinter(long id) {
        return Observable.just(mDiskManager.setActivePrinter(id));
    }

    @Override
    public Observable verifyPrinterDetailsEdit() {
        final long activeId = mDiskManager.getActivePrinterId();
        PrinterDbEntity old = mDiskManager.getPrinterByEditPrefId();

        Observable putEditInDb = mDiskManager.putEditPrinterDbEntityInDb();

        Observable verifyObs = mApiManager.getVersion().map(mDiskManager.putVersionInDb());

        return Observable.concat(putEditInDb, verifyObs)
                .doOnError(mDiskManager.deleteFailedEdit(old, activeId))
                .doOnCompleted(mDiskManager.resetActivePrinter(activeId));
    }
}
