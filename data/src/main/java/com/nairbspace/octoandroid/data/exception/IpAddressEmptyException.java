package com.nairbspace.octoandroid.data.exception;

public class IpAddressEmptyException extends Exception {

    public IpAddressEmptyException() {
    }

    public IpAddressEmptyException(String detailMessage) {
        super(detailMessage);
    }

    public IpAddressEmptyException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public IpAddressEmptyException(Throwable throwable) {
        super(throwable);
    }
}
