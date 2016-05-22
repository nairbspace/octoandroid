package com.nairbspace.octoandroid.ui.add_printer;

import android.content.Context;

public interface AddPrinterScreen {

    void showProgress(Boolean show);

    void showQrDialogFragment();

    void showIpAddressError(String error);

    void showSnackbar(String message);

    void showAlertDialog();

    void hideSoftKeyboard();

    void navigateToPreviousScreen();

    Context context();
}
