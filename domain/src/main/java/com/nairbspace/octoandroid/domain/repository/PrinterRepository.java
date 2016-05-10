package com.nairbspace.octoandroid.domain.repository;

import com.nairbspace.octoandroid.domain.AddPrinter;
import com.nairbspace.octoandroid.domain.Printer;
import com.nairbspace.octoandroid.domain.Version;

import rx.Observable;

public interface PrinterRepository {

    Observable<Printer> printerDetails();

    Observable<Printer> transformAddPrinter(AddPrinter addPrinter);

    Observable<Version> printerVersion(Printer printer);

    Observable<Boolean> deletePrinterDetails(Printer printer);

    Observable<Printer> printerDetails(String name);
}
