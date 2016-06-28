package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.ConnectEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.FileCommandEntity;
import com.nairbspace.octoandroid.data.entity.FilesEntity;
import com.nairbspace.octoandroid.data.entity.SlicerEntity;
import com.nairbspace.octoandroid.data.entity.SlicingCommandEntity;
import com.nairbspace.octoandroid.data.entity.ToolCommandEntity;
import com.nairbspace.octoandroid.data.entity.WebsocketEntity;
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

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.functions.Func1;


@Singleton
public class MapperHelper {

    private final EntitySerializer mEntitySerializer;

    @Inject
    public MapperHelper(EntitySerializer entitySerializer) {
        mEntitySerializer = entitySerializer;
    }

    public Func1<PrinterDbEntity, Printer> maptoPrinter() {
        return PrinterDbEntityMapper.maptoPrinter();
    }

    public Func1<List<PrinterDbEntity>, List<Printer>> mapToPrinters() {
        return PrinterDbEntityMapper.mapToPrinters();
    }

    public Observable.OnSubscribe<PrinterDbEntity> mapAddPrinterToPrinterDbEntity(AddPrinter addPrinter) {
        return PrinterDbEntityMapper.mapAddPrinterToPrinterDbEntity(addPrinter);
    }

    public Observable.OnSubscribe<PrinterDbEntity> mapPrinterToPrinterDbEntity(Printer printer) {
        return PrinterDbEntityMapper.mapPrinterToPrinterDbEntity(printer);
    }

    public Func1<ConnectionEntity, Connection> mapToConnection() {
        return ConnectionEntityMapper.mapToConnection(mEntitySerializer);
    }

    public Observable.OnSubscribe<ConnectEntity> mapToConnectEntity(Connect connect) {
        return ConnectEntityMapper.mapToConnectEntity(connect);
    }

    public Func1<FilesEntity, Files> mapToFiles() {
        return FilesEntityMapper.mapToFiles(mEntitySerializer);
    }

    public Func1<WebsocketEntity, Websocket> mapToWebsocket() {
        return WebsocketEntityMapper.maptoWebsocket(mEntitySerializer);
    }

    public Observable.OnSubscribe<FileCommandEntity> mapToFileCommandEntity(FileCommand fileCommand) {
        return FileCommandEntityMapper.mapToFileCommandEntity(fileCommand);
    }

    public Observable.OnSubscribe<MultipartBody.Part> mapToMultiPartBodyPart(String uriString) {
        return MultipartBodyPartMapper.mapToMultiPartBodyPart(uriString);
    }

    public Observable.OnSubscribe<Object> mapToTempCommandEntity(TempCommand tempCommand) {
        return TempCommandEntityMapper.mapToTempCommandEntity(tempCommand);
    }

    public Observable.OnSubscribe<Object> mapToPrintHeadCommandEntity(PrintHeadCommand printHeadCommand) {
        return PrintHeadCommandEntityMapper.mapToPrintHeadCommandEntity(printHeadCommand);
    }

    public ToolCommandEntity.Select mapToToolCommandEntitySelect(int tool) {
        return ToolCommandEntityMapper.mapToToolCommandEntitySelect(tool);
    }

    public Observable.OnSubscribe<Object> mapToToolCommandEntity(ToolCommand toolCommand) {
        return ToolCommandEntityMapper.mapToToolCommandEntity(toolCommand);
    }

    public Object mapToArbitraryCommandEntity(ArbitraryCommand arbitraryCommand) {
        return ArbitraryCommandEntityMapper.mapToArbitraryCommandEntity(arbitraryCommand);
    }

    public Func1<Map<String, SlicerEntity>, Map<String, Slicer>> mapToSlicer() {
        return SlicerEntityMapper.mapToSlicer(mEntitySerializer);
    }

    public Observable.OnSubscribe<SlicingCommandEntity> mapToSlicingCommandEntity(SlicingCommand slicingCommand) {
        return SlicingCommandEntityMapper.mapToEntity(slicingCommand);
    }
}
