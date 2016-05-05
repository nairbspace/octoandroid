package com.nairbspace.octoandroid.net.rest;

import com.nairbspace.octoandroid.net.rest.model.Connect;
import com.nairbspace.octoandroid.net.rest.model.Connection;
import com.nairbspace.octoandroid.net.rest.model.PrinterState;
import com.nairbspace.octoandroid.net.rest.model.Version;

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

    @GET("/api/printer")
    Observable<PrinterState> getPrinter();
}
