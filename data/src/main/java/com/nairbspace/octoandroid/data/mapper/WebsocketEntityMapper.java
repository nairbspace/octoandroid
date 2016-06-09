package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.entity.WebsocketEntity;
import com.nairbspace.octoandroid.domain.model.Websocket;

import rx.functions.Func1;

public class WebsocketEntityMapper {

    public static Func1<WebsocketEntity, Websocket> maptoWebsocket(final EntitySerializer entitySerializer) {
        return new Func1<WebsocketEntity, Websocket>() {
            @Override
            public Websocket call(WebsocketEntity websocketEntity) {
                return entitySerializer.transform(websocketEntity, Websocket.class);
            }
        };
    }
}
