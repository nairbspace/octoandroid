package com.nairbspace.octoandroid.data.net;

import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.PrinterStateEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class OctoApiImplDeprecated {
    private static OctoApiImplDeprecated sApiImpl;
    private OctoApi mApi;

    @Inject
    public OctoApiImplDeprecated(OctoApi api)  {
        mApi = api;
    }

    public Observable<VersionEntity> getVersionObservable() {
        return mApi.getVersion().cache();
    }

    public Observable<ConnectionEntity> getConnectionObservable() {
        return mApi.getConnection();
    }

//    public Observable<Boolean> postConnectObservable(ConnectEntity connectEntity) {
//        return mApi.postConnect(connectEntity);
//    }

    public Observable<PrinterStateEntity> getPrinterStateObservable() {
        return mApi.getPrinter();
    }
}
