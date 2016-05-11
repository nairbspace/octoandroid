package com.nairbspace.octoandroid.domain.repository;

import com.nairbspace.octoandroid.domain.pojo.AddPrinter;
import com.nairbspace.octoandroid.domain.pojo.Connection;
import com.nairbspace.octoandroid.domain.pojo.Printer;

import rx.Observable;

public interface PrinterRepository {

    Observable<Printer> printerDetails();

    Observable<Boolean> addPrinterDetails(AddPrinter addPrinter);

    Observable<Boolean> verifyPrinter();

    Observable<Boolean> deletePrinterDetails(Printer printer);

    Observable<Printer> printerDetails(String name);

    Observable<Connection> connectionDetails();
}
