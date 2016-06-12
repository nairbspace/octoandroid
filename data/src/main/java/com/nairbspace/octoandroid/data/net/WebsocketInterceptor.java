package com.nairbspace.octoandroid.data.net;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.disk.DbHelper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.exceptions.Exceptions;

public class WebsocketInterceptor implements Interceptor {

    private final DbHelper mDbHelper;

    @Inject
    public WebsocketInterceptor(DbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        PrinterDbEntity printerDbEntity = mDbHelper.getActivePrinterDbEntity();
        if (printerDbEntity == null) {
            return chain.proceed(original);
        }

        if (printerDbEntity.getScheme() != null && printerDbEntity.getHost() != null) {
            original = original.newBuilder()
                    .url(getWebsocketUrl(printerDbEntity))
                    .build();
        }
        return chain.proceed(original);
    }

    private String getWebsocketUrl(PrinterDbEntity printerDbEntity) {
        String scheme = printerDbEntity.getScheme();
        String host = printerDbEntity.getHost();
        String path = printerDbEntity.getWebsocketPath();

        String socketScheme = scheme.replace("http", "ws").replace("https", "wss");
        try {
            return new URI(socketScheme, host, path, null).toString();
        } catch (URISyntaxException e) {
            throw Exceptions.propagate(new URISyntaxException(e.getInput(), e.getReason()));
        }
    }
}
