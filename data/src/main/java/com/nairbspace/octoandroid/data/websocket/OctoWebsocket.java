package com.nairbspace.octoandroid.data.websocket;

import com.appunite.websocket.rx.RxWebSockets;
import com.appunite.websocket.rx.object.RxObjectWebSockets;
import com.appunite.websocket.rx.object.messages.RxObjectEventMessage;
import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.disk.DbHelper;
import com.nairbspace.octoandroid.data.exception.EntityMapperException;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import rx.Observable;
import rx.exceptions.Exceptions;

public class OctoWebsocket {
    private final Gson mGson;
    private final DbHelper mDbHelper;

    @Inject
    public OctoWebsocket(Gson gson, DbHelper dbHelper) {
        mGson = gson;
        mDbHelper = dbHelper;
    }

    public Observable<WebsocketEntity> getWebsocketObservable() {
        RxWebSockets rxWebSockets = new RxWebSockets(new OkHttpClient(), getRequest());
        GsonObjectSerializer gsonObjectSerializer = new GsonObjectSerializer(mGson, WebsocketEntity.class);
        RxObjectWebSockets rxObjectWebSockets = new RxObjectWebSockets(rxWebSockets, gsonObjectSerializer);
        return rxObjectWebSockets.webSocketObservable()
                .compose(MoreObservables.filterAndMap(RxObjectEventMessage.class))
                .compose(RxObjectEventMessage.filterAndMap(WebsocketEntity.class));
    }

    private Request getRequest() {
        return new Request.Builder()
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
            e.printStackTrace();
            throw Exceptions.propagate(new EntityMapperException()); // TODO should be websocket specific
        }
    }
}
