package com.nairbspace.octoandroid.exception;

public class TransformErrorException extends Exception {

    public TransformErrorException() {
    }

    public TransformErrorException(String detailMessage) {
        super(detailMessage);
    }

    public TransformErrorException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TransformErrorException(Throwable throwable) {
        super(throwable);
    }
}
