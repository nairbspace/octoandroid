package com.nairbspace.octoandroid.data.exception;

public class PrinterDataNotFoundException extends Exception {
    
    public PrinterDataNotFoundException() {
    }

    public PrinterDataNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    public PrinterDataNotFoundException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

    public PrinterDataNotFoundException(Throwable cause) {
        super(cause);
    }
}
