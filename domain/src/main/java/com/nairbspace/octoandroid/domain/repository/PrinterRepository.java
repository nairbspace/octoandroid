package com.nairbspace.octoandroid.domain.repository;

import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.domain.model.Printer;

import rx.Observable;

public interface PrinterRepository {

    Observable<Printer> printerDetails();

    Observable<Boolean> addPrinterDetails(AddPrinter addPrinter);

    Observable<Boolean> deletePrinterByName(String name);

    Observable<Connection> connectionDetails();

    Observable<Boolean> connectToPrinter(Connect connect);
}
