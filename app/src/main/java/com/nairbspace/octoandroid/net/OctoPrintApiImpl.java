package com.nairbspace.octoandroid.net;

import javax.inject.Inject;
import javax.inject.Singleton;

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

//    private Observable<Version> mVersionObservable = mApi.getVersion().cache();
//
//    public Observable<Version> getVersionObservable() {
//        return mVersionObservable;
//    }
}
