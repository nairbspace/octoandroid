package com.nairbspace.octoandroid.mapper;

import com.google.gson.Gson;

public class ModelSerializer {

    private final Gson mGson;

    public ModelSerializer(Gson gson) {
        mGson = gson;
    }

    /**
     *
     * @param inputObject Input object
     * @param outputType Output class
     * @param <T> Generic output
     * @return Output object
     */
    public <T> T transform(Object inputObject, Class<T> outputType) {
        String json = mGson.toJson(inputObject);
        return mGson.fromJson(json, outputType);
    }
}
