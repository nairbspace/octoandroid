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

    private ErrorMessageFactory() {
        // TODO need better way to implement this
    }

    public static String create(Context context, Exception e) {
        String message = context.getString(R.string.exception_message_generic);

        if (e instanceof NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if (e instanceof PrinterDataNotFoundException) {
            message = context.getString(R.string.exception_message_printer_not_found);
        } else if (e instanceof IpAddressEmptyException) {
            message = context.getString(R.string.exception_ip_address_blank);
        } else if (e instanceof IncorrectAddPrinterFormattingException) {
            message = context.getString(R.string.exception_incorrect_ip_address_formatting);
        } else if (e instanceof ConnectException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if (e.getMessage().contains(context.getString(R.string.exception_ssl_error))) {
            message = context.getString(R.string.ssl_error_display_message);
        } else if (e.getMessage().contains(context.getString(R.string.exception_invalid_api_key))) {
            message = context.getString(R.string.exception_invalid_api_key);
        }
        return message;
    }

    public static boolean ifNoInternet(Exception e) {
        return e instanceof NetworkConnectionException || e instanceof ConnectException;
    }

    public static boolean isThereNoActivePrinter(Exception exception) {
        return (exception instanceof NoActivePrinterException);
    }

    public static boolean isIpAddressError(Exception exception) {
        return exception instanceof IpAddressEmptyException ||
                exception instanceof IncorrectAddPrinterFormattingException;
    }

    public static boolean ifSslError(Context context, String error) {
        return error.contains(context.getResources().getString(R.string.exception_ssl_error));
    }
}
