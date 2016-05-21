package com.nairbspace.octoandroid.domain.repository;

import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.domain.model.FileCommand;
import com.nairbspace.octoandroid.domain.model.Files;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.domain.model.Websocket;

import java.util.HashMap;

import rx.Observable;

public interface PrinterRepository {

    Observable<Printer> printerDetails();

    Observable verifyPrinterDetails(AddPrinter addPrinter);

    Observable addPrinterDetails(AddPrinter addPrinter);

    Observable<Boolean> deletePrinterByName(String name);

    Observable<Connection> connectionDetails();

    Observable connectToPrinter(Connect connect);

    Observable<Files> getAllFiles();

    Observable<Websocket> getWebsocket();

    Observable sendJobCommand(HashMap<String, String> command);

    Observable sendFileCommand(FileCommand fileCommand);

    Observable deleteFile(String url);

    Observable uploadFile(String uriString);
}
