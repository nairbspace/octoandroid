package com.nairbspace.octoandroid.ui.printer_settings;

import com.nairbspace.octoandroid.model.PrinterModel;

import java.util.List;

public interface PrinterListScreen {

    void navigateToPrinterDetailsActivity(int position);

    void updateUi(List<PrinterModel> printerModels);

    void showSnackbar(String message);

    void showEditFailure();

    void navigateToStatusActivity();

    void deleteFromAdapter(int position);

    void setRefreshing(boolean enable);
}
