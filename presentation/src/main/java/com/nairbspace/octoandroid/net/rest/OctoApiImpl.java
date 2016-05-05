package com.nairbspace.octoandroid.net.rest;

import com.nairbspace.octoandroid.net.rest.model.Connect;
import com.nairbspace.octoandroid.net.rest.model.Connection;
import com.nairbspace.octoandroid.net.rest.model.PrinterState;
import com.nairbspace.octoandroid.net.rest.model.Version;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class OctoApiImpl {
    private static OctoApiImpl sApiImpl;
    private OctoApi mApi;

    @Inject
    public OctoApiImpl(OctoApi api)  {
        mApi = api;
    }

    public Observable<Version> getVersionObservable() {
        return mApi.getVersion().cache();
    }

    public Observable<Connection> getConnectionObservable() {
        return mApi.getConnection();
    }

    public Observable<Connect> postConnectObservable(Connect connect) {
        return mApi.postConnect(connect);
    }

    public Observable<PrinterState> getPrinterStateObservable() {
        return mApi.getPrinter();
    }
}
