package com.nairbspace.octoandroid.data.exception;

public class NoActivePrinterException extends Exception {

    public NoActivePrinterException() {
    }

    public NoActivePrinterException(String message) {
        super(message);
    }

    public NoActivePrinterException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoActivePrinterException(Throwable cause) {
        super(cause);
    }
}
