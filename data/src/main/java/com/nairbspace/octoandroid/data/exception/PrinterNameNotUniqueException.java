package com.nairbspace.octoandroid.data.exception;

public class PrinterNameNotUniqueException extends Exception {

    public PrinterNameNotUniqueException() {
    }

    public PrinterNameNotUniqueException(String detailMessage) {
        super(detailMessage);
    }

    public PrinterNameNotUniqueException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public PrinterNameNotUniqueException(Throwable throwable) {
        super(throwable);
    }
}
