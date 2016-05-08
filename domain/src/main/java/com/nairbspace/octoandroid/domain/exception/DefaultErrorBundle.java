package com.nairbspace.octoandroid.domain.exception;

public class DefaultErrorBundle implements ErrorBundle {

    private static final String DEFAULT_ERROR_MSG = "Unknown error";

    private final Exception mException;

    public DefaultErrorBundle(Exception exception) {
        mException = exception;
    }

    @Override
    public Exception getException() {
        return mException;
    }

    @Override
    public String getErrorMessage() {
        return (mException != null) ? mException.getMessage() : DEFAULT_ERROR_MSG;
    }
}
