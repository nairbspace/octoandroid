package com.nairbspace.octoandroid.net;

import com.nairbspace.octoandroid.net.model.Connect;
import com.nairbspace.octoandroid.net.model.Connection;
import com.nairbspace.octoandroid.net.model.Version;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface OctoApi {

    @GET("/api/version")
    Observable<Version> getVersion();

    @GET("/api/connection")
    Observable<Connection> getConnection();

    @POST("/api/connection")
    Observable<Connect> postConnect(@Body Connect connect);
}
