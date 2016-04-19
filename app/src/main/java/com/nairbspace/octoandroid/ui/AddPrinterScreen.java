package com.nairbspace.octoandroid.ui;

public interface AddPrinterScreen {

    void showProgress(Boolean show);

    void showQrDialogFragment();

    void showIpAddressError(String error);

    void showSnackbar(String message);

    void showAlertDialog(String title, String message);

    void hideSoftKeyboard(boolean show);

}
