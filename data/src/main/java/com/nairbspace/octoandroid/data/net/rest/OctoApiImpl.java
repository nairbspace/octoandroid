package com.nairbspace.octoandroid.data.net.rest;

import com.nairbspace.octoandroid.data.entity.ConnectEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.PrinterStateEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

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

    public Observable<VersionEntity> getVersionObservable() {
        return mApi.getVersion().cache();
    }

    public Observable<ConnectionEntity> getConnectionObservable() {
        return mApi.getConnection();
    }

    public Observable<ConnectEntity> postConnectObservable(ConnectEntity connectEntity) {
        return mApi.postConnect(connectEntity);
    }

    public Observable<PrinterStateEntity> getPrinterStateObservable() {
        return mApi.getPrinter();
    }
}
