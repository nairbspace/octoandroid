package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.exception.EntityMapperException;
import com.nairbspace.octoandroid.domain.model.Connection;

import javax.inject.Inject;

import rx.exceptions.Exceptions;
import rx.functions.Func1;

public class ConnectionEntityMapper {

    private final JsonSerializer mJsonSerializer;

    @Inject
    public ConnectionEntityMapper(JsonSerializer jsonSerializer) {
        mJsonSerializer = jsonSerializer;
    }

    public Func1<ConnectionEntity, Connection> mapToConnection() {
        return new Func1<ConnectionEntity, Connection>() {
            @Override
            public Connection call(ConnectionEntity connectionEntity) {
                try {
                    return mJsonSerializer.transform(connectionEntity, Connection.class);
                } catch (Exception e) {
                    throw Exceptions.propagate(new EntityMapperException(e));
                }
            }
        };
    }
}
