package com.nairbspace.octoandroid.data.net;

import com.nairbspace.octoandroid.data.entity.ConnectEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.FileCommandEntity;
import com.nairbspace.octoandroid.data.entity.FilesEntity;
import com.nairbspace.octoandroid.data.entity.PrinterStateEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import java.util.HashMap;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface OctoApi {

    @GET("/api/version")
    Observable<VersionEntity> getVersion();

    @GET("/api/connection")
    Observable<ConnectionEntity> getConnection();

    @POST("/api/connection")
    Observable<Object> postConnect(@Body ConnectEntity connectEntity);

    @GET("/api/printerDetails")
    Observable<PrinterStateEntity> getPrinter();

    @GET("/api/files")
    Observable<FilesEntity> getAllFiles();

    @POST("/api/job")
    Observable<Object> sendJobCommand(@Body HashMap<String, String> command);

    @POST
    Observable<Object> startFilePrint(@Url String url, @Body FileCommandEntity fileCommandEntity);

    @DELETE
    Observable<Object> deleteFile(@Url String url);
}
