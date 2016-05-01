package com.nairbspace.octoandroid.net;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class OctoApiImpl {
    private static OctoApiImpl sApiImpl;
    private OctoApi mApi;

    @Inject
    public static OctoApiImpl get(OctoApi api) {
        if (sApiImpl == null) {
            sApiImpl = new OctoApiImpl(api);
        }

        return sApiImpl;
    }

    private OctoApiImpl(OctoApi api)  {
        mApi = api;
    }

    public Observable<Version> getVersionObservable() {
        return mApi.getVersion().cache();
    }

    public Observable<Connection> getConnectionObservable() {
        return mApi.getConnection().cache();
    }

    public Observable<Connect> postConnectObservable(Connect connect) {
        return mApi.postConnect(connect).cache();
    }
}
