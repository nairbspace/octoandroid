package com.nairbspace.octoandroid.data.exception;

import com.nairbspace.octoandroid.domain.exception.ErrorBundle;

public class RepositoryErrorBundle implements ErrorBundle {

    private final Exception mException;

    public RepositoryErrorBundle(Exception exception) {
        mException = exception;
    }

    @Override
    public Exception getException() {
        return mException;
    }

    @Override
    public String getErrorMessage() {
        String message = "";
        if (mException != null) {
            return mException.getMessage();
        }
        return message;
    }
}
