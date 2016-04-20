package com.nairbspace.octoandroid.net;

import com.nairbspace.octoandroid.model.Version;

import retrofit2.http.GET;
import rx.Observable;

public interface OctoApi {

    @GET("/api/version")
    Observable<Version> getVersion();

}
