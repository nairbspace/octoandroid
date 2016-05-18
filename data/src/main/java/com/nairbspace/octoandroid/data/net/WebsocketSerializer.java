package com.nairbspace.octoandroid.data.net;

import com.appunite.websocket.rx.object.ObjectParseException;
import com.appunite.websocket.rx.object.ObjectSerializer;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.nairbspace.octoandroid.data.entity.WebsocketEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class WebsocketSerializer implements ObjectSerializer {

    private final Gson mGson;

    @Inject
    public WebsocketSerializer(Gson gson) {
        mGson = gson;
    }

    @Nonnull
    @Override
    public Object serialize(@Nonnull String message) throws ObjectParseException {
        try {
            return mGson.fromJson(message, WebsocketEntity.class); // Take note of WebsocketEntity.class if want to change!
        } catch (JsonParseException e) {
            throw new ObjectParseException("Could not parse", e);
        }
    }

    @Nonnull
    @Override
    public Object serialize(@Nonnull byte[] message) throws ObjectParseException {
        throw new ObjectParseException("Could not parse binary messages");
    }

    @Nonnull
    @Override
    public byte[] deserializeBinary(@Nonnull Object message) throws ObjectParseException {
        return new byte[0];
    }

    @Nonnull
    @Override
    public String deserializeString(@Nonnull Object message) throws ObjectParseException {
        try {
            return mGson.toJson(message);
        } catch (JsonParseException e) {
            throw new ObjectParseException("Could not parse", e);
        }
    }

    @Override
    public boolean isBinary(@Nonnull Object message) {
        return false;
    }
}
