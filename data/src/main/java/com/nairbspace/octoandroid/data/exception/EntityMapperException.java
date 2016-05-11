package com.nairbspace.octoandroid.data.exception;

public class EntityMapperException extends Exception {

    public EntityMapperException() {
    }

    public EntityMapperException(String detailMessage) {
        super(detailMessage);
    }

    public EntityMapperException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public EntityMapperException(Throwable throwable) {
        super(throwable);
    }

}
