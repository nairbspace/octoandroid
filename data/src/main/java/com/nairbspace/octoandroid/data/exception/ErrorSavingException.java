package com.nairbspace.octoandroid.data.exception;

public class ErrorSavingException extends Exception {

    public ErrorSavingException() {
    }

    public ErrorSavingException(String detailMessage) {
        super(detailMessage);
    }

    public ErrorSavingException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ErrorSavingException(Throwable throwable) {
        super(throwable);
    }
}
