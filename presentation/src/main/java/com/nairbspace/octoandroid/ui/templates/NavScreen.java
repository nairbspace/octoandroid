package com.nairbspace.octoandroid.ui.templates;

import android.content.Context;

public interface NavScreen {

    void updateNavHeader(String printerName, String ipAddress);

    void displaySnackBar(String message);

    void displayNoActivePrinterSnackBar();

    void hideSnackbar();

    Context context();
}
