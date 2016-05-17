package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.websocket.WebsocketEntity;
import com.nairbspace.octoandroid.domain.model.Websocket;

import javax.inject.Inject;

import rx.functions.Func1;

public class WebsocketEntityMapper {

    private final EntitySerializer mEntitySerializer;

    @Inject
    public WebsocketEntityMapper(EntitySerializer entitySerializer) {
        mEntitySerializer = entitySerializer;
    }

    public Func1<WebsocketEntity, Websocket> maptoWebsocket() {
        return new Func1<WebsocketEntity, Websocket>() {
            @Override
            public Websocket call(WebsocketEntity websocketEntity) {
                return mEntitySerializer.transform(websocketEntity, Websocket.class);
            }
        };
    }
}
