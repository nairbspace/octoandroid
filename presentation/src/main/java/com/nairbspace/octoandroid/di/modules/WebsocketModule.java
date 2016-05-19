package com.nairbspace.octoandroid.di.modules;

import com.appunite.websocket.rx.RxWebSockets;
import com.appunite.websocket.rx.object.ObjectSerializer;
import com.appunite.websocket.rx.object.RxObjectWebSockets;
import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.net.WebsocketManager;
import com.nairbspace.octoandroid.data.net.WebsocketManagerImpl;
import com.nairbspace.octoandroid.data.net.WebsocketSerializer;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Module
public class WebsocketModule {
    private static final String DUMMY_WEBSOCKET = "ws://localhost";

    @Provides
    @Singleton
    ObjectSerializer provideObjectSerializer(Gson gson) {
        return new WebsocketSerializer(gson);
    }

    @Provides
    @Singleton
    Request provideWebsocketRequest() {
        return new Request.Builder()
                .get()
                .url(DUMMY_WEBSOCKET) // Gets changed from interceptor
                .build();
    }

    @Provides
    @Singleton
    RxWebSockets provideRxWebsockets(@Named("websocket") OkHttpClient okHttpClient,
                                     Request request) {
        return new RxWebSockets(okHttpClient, request);
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
