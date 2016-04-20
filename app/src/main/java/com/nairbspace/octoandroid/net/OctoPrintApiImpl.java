package com.nairbspace.octoandroid.net;

import com.nairbspace.octoandroid.model.Version;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class OctoPrintApiImpl {
    private static OctoPrintApiImpl sApiImpl;
    private OctoApi mApi;

    @Inject
    public static OctoPrintApiImpl get(OctoApi api) {
        if (sApiImpl == null) {
            sApiImpl = new OctoPrintApiImpl(api);
        }

        return sApiImpl;
    }

    private OctoPrintApiImpl(OctoApi api)  {
        mApi = api;
    }

    public Observable<Version> getVersionObservable() {
        return mApi.getVersion().cache();
    }
}
