package com.nairbspace.octoandroid.domain.exception;

public class NullUseCaseBuilderException extends Exception {

    public NullUseCaseBuilderException() {
    }

    public NullUseCaseBuilderException(String message) {
        super(message);
    }

    public NullUseCaseBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullUseCaseBuilderException(Throwable cause) {
        super(cause);
    }
}
