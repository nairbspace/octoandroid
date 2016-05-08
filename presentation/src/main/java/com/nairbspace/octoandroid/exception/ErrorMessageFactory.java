package com.nairbspace.octoandroid.exception;

import android.content.Context;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.data.exception.NetworkConnectionException;
import com.nairbspace.octoandroid.data.exception.NoActivePrinterException;
import com.nairbspace.octoandroid.data.exception.PrinterDataNotFoundException;

public class ErrorMessageFactory {

    private ErrorMessageFactory() {
    }

    public static String create(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_generic);

        if (exception instanceof NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if (exception instanceof PrinterDataNotFoundException) {
            message = context.getString(R.string.exception_message_printer_not_found);
        }
        return message;
    }

    public static boolean isThereNoActivePrinter(Exception exception) {
        return (exception instanceof NoActivePrinterException);
    }
}
