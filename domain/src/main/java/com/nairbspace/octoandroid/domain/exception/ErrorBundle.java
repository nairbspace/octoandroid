package com.nairbspace.octoandroid.domain.exception;

public interface ErrorBundle {
    Exception getException();

    String getErrorMessage();
}
