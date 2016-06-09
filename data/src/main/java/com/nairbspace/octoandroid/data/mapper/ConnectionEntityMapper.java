package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.exception.EntityMapperException;
import com.nairbspace.octoandroid.domain.model.Connection;

import rx.exceptions.Exceptions;
import rx.functions.Func1;

public class ConnectionEntityMapper {

    public static Func1<ConnectionEntity, Connection> mapToConnection(final EntitySerializer entitySerializer) {
        return new Func1<ConnectionEntity, Connection>() {
            @Override
            public Connection call(ConnectionEntity connectionEntity) {
                try {
                    return entitySerializer.transform(connectionEntity, Connection.class);
                } catch (Exception e) {
                    throw Exceptions.propagate(new EntityMapperException(e));
                }
            }
        };
    }
}
