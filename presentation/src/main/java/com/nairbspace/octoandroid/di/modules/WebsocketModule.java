package com.nairbspace.octoandroid.di.modules;

import com.appunite.websocket.rx.RxWebSockets;
import com.appunite.websocket.rx.object.ObjectSerializer;
import com.appunite.websocket.rx.object.RxObjectWebSockets;
import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.disk.DbHelper;
import com.nairbspace.octoandroid.data.net.RequestBuilder;
import com.nairbspace.octoandroid.data.net.WebsocketManager;
import com.nairbspace.octoandroid.data.net.WebsocketManagerImpl;
import com.nairbspace.octoandroid.data.net.WebsocketSerializer;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class WebsocketModule {

    @Provides
    @Singleton
    ObjectSerializer provideObjectSerializer(Gson gson) {
        return new WebsocketSerializer(gson);
    }

    @Provides
    @Singleton
    RequestBuilder provideRequestBuilder(DbHelper dbHelper) {
        return new RequestBuilder(dbHelper);
    }

    @Provides
    @Singleton
    RxWebSockets provideRxWebsockets(@Named("regular") OkHttpClient okHttpClient,
                                     RequestBuilder requestBuilder) {
        return new RxWebSockets(okHttpClient, requestBuilder.getRequest());
    }

    @Provides
    @Singleton
    RxObjectWebSockets provideRxObjectWebsockets(RxWebSockets rxWebSockets, ObjectSerializer objectSerializer) {
        return new RxObjectWebSockets(rxWebSockets, objectSerializer);
    }

    @Provides
    @Singleton
    WebsocketManager provideWebsocketManager(RxObjectWebSockets rxObjectWebSockets) {
        return new WebsocketManagerImpl(rxObjectWebSockets);
    }
}
