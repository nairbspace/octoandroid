package com.nairbspace.octoandroid.data.websocket;

import com.appunite.websocket.rx.object.ObjectParseException;
import com.appunite.websocket.rx.object.ObjectSerializer;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import javax.annotation.Nonnull;

public class GsonObjectSerializer implements ObjectSerializer {

    private final Gson mGson;
    private final Type mType;

    public GsonObjectSerializer(Gson gson, Type type) {
        mGson = gson;
        mType = type;
    }

    @Nonnull
    @Override

    public Object serialize(@Nonnull String message) throws ObjectParseException {
        try {
            return mGson.fromJson(message, mType);
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
