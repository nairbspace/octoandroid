package com.nairbspace.octoandroid.data.net;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.AddPrinterEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import rx.Observable;
import rx.functions.Func1;

public interface ApiManager extends OctoApi {

    Observable<PrinterDbEntity> map(Observable<AddPrinterEntity> addPrinterEntityObs);

    Func1<PrinterDbEntity, Observable<VersionEntity>> concatGetVersion();

}
