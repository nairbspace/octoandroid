package com.nairbspace.octoandroid.data.repository.datasource;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;

import rx.Observable;

public interface PrinterDataStore {

    Observable<PrinterDbEntity> printerEntityDetails();
}
