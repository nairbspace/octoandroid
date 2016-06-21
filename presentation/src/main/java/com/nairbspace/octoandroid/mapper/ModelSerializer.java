package com.nairbspace.octoandroid.mapper;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class ModelSerializer {

    private final Gson mGson;

    @Inject public ModelSerializer(Gson gson) {
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

    /**
     * Transforms object to the specified type
     * @param inputObject Input object
     * @param type Type example: new TypeToken< Map< String, Slicer>>(){}.getType();
     * @param <T> In example: Map< String, Slicer>
     * @return the transformed object
     */
    public <T> T transform(Object inputObject, Type type) {
        String json = mGson.toJson(inputObject);
        return mGson.fromJson(json, type);
    }
}
