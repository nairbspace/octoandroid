package com.nairbspace.octoandroid.exception;

import android.content.Context;
import android.content.res.Resources;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.data.exception.IncorrectAddPrinterFormattingException;
import com.nairbspace.octoandroid.data.exception.IpAddressEmptyException;
import com.nairbspace.octoandroid.data.exception.NetworkConnectionException;
import com.nairbspace.octoandroid.data.exception.NoActivePrinterException;
import com.nairbspace.octoandroid.data.exception.PrinterDataNotFoundException;

public class ErrorMessageFactory {

    private ErrorMessageFactory() {
        // TODO need better way to implement this
    }

    public static String create(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_generic);

        if (exception instanceof NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if (exception instanceof PrinterDataNotFoundException) {
            message = context.getString(R.string.exception_message_printer_not_found);
        } else if (exception instanceof IpAddressEmptyException) {
            message = context.getString(R.string.exception_ip_address_blank);
        } else if (exception instanceof IncorrectAddPrinterFormattingException) {
            message = context.getString(R.string.exception_incorrect_ip_address_formatting);
        }
        return message;
    }

    public static boolean isThereNoActivePrinter(Exception exception) {
        return (exception instanceof NoActivePrinterException);
    }

    public static boolean isIpAddressError(Exception exception) {
        return exception instanceof IpAddressEmptyException ||
                exception instanceof IncorrectAddPrinterFormattingException;
    }

    public static String createIpAddressError(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_generic);

        if (exception instanceof IpAddressEmptyException) {
            message = context.getString(R.string.exception_ip_address_blank);
        } else if (exception instanceof IncorrectAddPrinterFormattingException) {
            message = context.getString(R.string.exception_incorrect_ip_address_formatting);
        }
        return message;
    }

    public static String createGetVersionError(Context context, String error) {
        Resources resources = context.getResources();
        String message = resources.getString(R.string.exception_message_generic);
        if (error.contains(resources.getString(R.string.exception_ssl_error))) {
            message = resources.getString(R.string.ssl_error_display_message);
        } else if (error.contains(resources.getString(R.string.exception_invalid_api_key))) {
            message = resources.getString(R.string.exception_invalid_api_key);
        }

        return message;
    }

    public static boolean ifSslError(Context context, String error) {
        return error.contains(context.getResources().getString(R.string.exception_ssl_error));
    }

    public static String getSslTitle(Context context) {
        return context.getResources().getString(R.string.ssl_error_title);
    }
}
