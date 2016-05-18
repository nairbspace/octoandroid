package com.nairbspace.octoandroid.data.net;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.disk.DbHelper;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import okhttp3.Request;
import rx.exceptions.Exceptions;

public class RequestBuilder {

    private final DbHelper mDbHelper;
    private Request mRequest;

    @Inject
    public RequestBuilder(DbHelper dbHelper) {
        mDbHelper = dbHelper;
        renewRequest();
    }

    public Request getRequest() {
        return mRequest;
    }

    public void renewRequest() { // TODO this should be called when there are multiple printers and new one is set
        mRequest = new Request.Builder()
                .get()
                .url(getWebsocketUrl())
                .build();
    }

    private String getWebsocketUrl() {
        PrinterDbEntity printerDbEntity = mDbHelper.getActivePrinterDbEntity();
        String scheme = printerDbEntity.getScheme();
        String host = printerDbEntity.getHost();
        String path = "/sockjs/websocket";

        String socketScheme = scheme.replace("http", "ws").replace("https", "wss");
        try {
            return new URI(socketScheme, host, path, null).toString();
        } catch (URISyntaxException e) {
            throw Exceptions.propagate(new URISyntaxException(e.getInput(), e.getReason()));
        }
    }
}
