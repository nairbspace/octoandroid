package com.nairbspace.octoandroid.data.repository.datasource;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.AddPrinterEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import rx.Observable;

public interface PrinterDataStore {

    Observable<PrinterDbEntity> printerDbEntityDetails();

    Observable<PrinterDbEntity> transformAddPrinterEntity(AddPrinterEntity addPrinterEntity);

    Observable<VersionEntity> printerVersionEntity(PrinterDbEntity printerDbEntity);

    Observable<Boolean> deletePrinterDbEntityDetails(PrinterDbEntity printerDbEntity);

    Observable<PrinterDbEntity> printerDbEntityDetails(String name);
}
