package com.nairbspace.octoandroid.data.net.rest;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class OctoInterceptor implements Interceptor {
    private static OctoInterceptor sInterceptor;
    private String mScheme;
    private String mHost;
    private int mPort;
    private String mApiKey;

    @Inject
    public OctoInterceptor() {
    }

    public void setInterceptor(String scheme, String host, int port, String apiKey) {
        mScheme = scheme;
        mHost = host;
        mPort = port;
        mApiKey = apiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        if (mScheme != null && mHost != null && mApiKey != null) {
            HttpUrl newUrl = original.url().newBuilder()
                    .scheme(mScheme)
                    .host(mHost)
                    .port(mPort)
                    .build();
            original = original.newBuilder()
                    .url(newUrl)
                    .header("X-Api-Key", mApiKey)
                    .build();
        } // TODO need another interceptor for uploading files or different headers
        return chain.proceed(original);
    }
}
