package com.nairbspace.octoandroid.data.net;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.AddPrinterEntity;

import rx.Observable;

public interface ApiManager extends OctoApi {

    Observable<PrinterDbEntity> map(Observable<AddPrinterEntity> addPrinterEntityObs);

}
