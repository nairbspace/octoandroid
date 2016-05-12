package com.nairbspace.octoandroid.data.net;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.ConnectEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import rx.Observable;
import rx.functions.Func1;

public interface ApiManager extends OctoApi {

    Func1<PrinterDbEntity, Observable<VersionEntity>> funcGetVersion();

    Func1<ConnectionEntity, Observable<ConnectionEntity>> funcGetConnection();

    Func1<ConnectEntity, Observable<ConnectEntity>> connectToPrinter();

    Func1<ConnectEntity, Boolean> connectPrinterResult();

}
