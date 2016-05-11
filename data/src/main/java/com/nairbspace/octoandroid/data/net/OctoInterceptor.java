package com.nairbspace.octoandroid.data.net;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.disk.DbHelper;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class OctoInterceptor implements Interceptor {

    private final DbHelper mDbHelper;

    @Inject
    public OctoInterceptor(DbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        PrinterDbEntity printerDbEntity = mDbHelper.getActivePrinterDbEntity();
        if (printerDbEntity == null) {
            return chain.proceed(original);
        }

        if (printerDbEntity.getScheme() != null && printerDbEntity.getHost() != null &&
                printerDbEntity.getApiKey() != null) {
            HttpUrl newUrl = original.url().newBuilder()
                    .scheme(printerDbEntity.getScheme())
                    .host(printerDbEntity.getHost())
                    .port(printerDbEntity.getPort())
                    .build();
            original = original.newBuilder()
                    .url(newUrl)
                    .header("X-Api-Key", printerDbEntity.getApiKey())
                    .build();
        } // TODO need another interceptor for uploading files or different headers
        return chain.proceed(original);
    }
}
