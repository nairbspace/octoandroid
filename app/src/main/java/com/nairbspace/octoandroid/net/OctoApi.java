package com.nairbspace.octoandroid.net;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OctoApi {

    @GET("/api/version")
    Call<Version> getVersion();

}
