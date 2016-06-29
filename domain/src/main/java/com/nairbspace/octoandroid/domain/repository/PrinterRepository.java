package com.nairbspace.octoandroid.domain.repository;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

public interface PrinterRepository {

    Observable<Boolean> isDbEmpty();

    Observable<Printer> printerDetails();

    Observable<List<Printer>> getPrinters();

    Observable addPrinterDetails(AddPrinter addPrinter);

    Observable setPrinterPrefs(long id);

    Observable<Boolean> syncDbAndAccountDeletion(String name);

    Observable<Boolean> deletePrinterById(long id);

    Observable<Connection> connectionDetails();

    Observable connectToPrinter(Connect connect);

    Observable<Files> getAllFiles();

    Observable<Websocket> getWebsocket();

    Observable sendJobCommand(HashMap<String, String> command);

    Observable sendFileCommand(FileCommand fileCommand);

    Observable deleteFile(String url);

    Observable uploadFile(String uriString);

    Observable connectToWebcam();

    Observable sendTempCommand(TempCommand tempCommand);

    Observable sendPrintHeadCommand(PrintHeadCommand command);

    Observable selectTool(int tool);

    Observable sendToolCommand(ToolCommand toolCommand);

    Observable sendArbitraryCommand(ArbitraryCommand arbitraryCommand);

    Observable<Boolean> isPushNotificationOn();

    Observable<Boolean> isStickyNotificationOn();

    Observable<Map<String, Slicer>> getSlicers();

    Observable sendSliceCommand(SlicingCommand slicingCommand);

    Observable setActivePrinter(long id);
}
