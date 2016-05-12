package com.nairbspace.octoandroid.data.mapper;

import com.google.gson.Gson;

import javax.inject.Inject;

public class EntitySerializer {

    private final Gson mGson;

    @Inject
    public EntitySerializer(Gson gson) {
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
     *
     * @param json String json input
     * @param inputClass Entity class
     * @param <T> Entity class
     * @return Entity object
     */
    public <T> T deserialize(String json, Class<T> inputClass) {
        return mGson.fromJson(json, inputClass);
    }

    /**
     *
     * @param inputObject input Object
     * @return Json string
     */
    public String serialize(Object inputObject) {
        return mGson.toJson(inputObject);
    }
}
