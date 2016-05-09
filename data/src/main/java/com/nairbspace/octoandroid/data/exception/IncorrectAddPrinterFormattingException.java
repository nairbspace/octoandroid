package com.nairbspace.octoandroid.data.exception;

public class IncorrectAddPrinterFormattingException extends Exception {

    public IncorrectAddPrinterFormattingException() {
    }

    public IncorrectAddPrinterFormattingException(String detailMessage) {
        super(detailMessage);
    }

    public IncorrectAddPrinterFormattingException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public IncorrectAddPrinterFormattingException(Throwable throwable) {
        super(throwable);
    }
}
