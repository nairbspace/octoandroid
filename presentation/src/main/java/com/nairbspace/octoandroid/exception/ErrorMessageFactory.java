package com.nairbspace.octoandroid.exception;

import android.content.Context;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.data.exception.IncorrectAddPrinterFormattingException;
import com.nairbspace.octoandroid.data.exception.IpAddressEmptyException;
import com.nairbspace.octoandroid.data.exception.NetworkConnectionException;
import com.nairbspace.octoandroid.data.exception.NoActivePrinterException;
import com.nairbspace.octoandroid.data.exception.PrinterDataNotFoundException;

import java.net.ConnectException;

public class ErrorMessageFactory {

    public static String create(Context context, Throwable t) {
        String message = context.getString(R.string.exception_message_generic);

        if (t instanceof NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if (t instanceof PrinterDataNotFoundException) {
            message = context.getString(R.string.exception_message_printer_not_found);
        } else if (t instanceof IpAddressEmptyException) {
            message = context.getString(R.string.exception_ip_address_blank);
        } else if (t instanceof IncorrectAddPrinterFormattingException) {
            message = context.getString(R.string.exception_incorrect_ip_address_formatting);
        } else if (t instanceof ConnectException) {
            message = context.getString(R.string.exception_message_connecting);
        }

        //  Message can be null such as in the event of a SocketTimeOutException
        if (t.getMessage() != null) {
            if (t.getMessage().contains(context.getString(R.string.exception_ssl_error))) {
                message = context.getString(R.string.ssl_error_display_message);
            } else if (t.getMessage().contains(context.getString(R.string.exception_invalid_api_key))) {
                message = context.getString(R.string.exception_invalid_api_key);
            }
        }

        return message;
    }

    public static boolean ifNoInternet(Throwable t) {
        return t instanceof NetworkConnectionException || t instanceof ConnectException;
    }

    public static boolean isThereNoActivePrinter(Throwable t) {
        return (t instanceof NoActivePrinterException);
    }

    public static boolean isIpAddressError(Throwable t) {
        return t instanceof IpAddressEmptyException ||
                t instanceof IncorrectAddPrinterFormattingException;
    }

    public static boolean ifSslError(Context context, Throwable t) {
        return t.getMessage() != null && t.getMessage().contains(context.getResources().getString(R.string.exception_ssl_error));
    }
}
