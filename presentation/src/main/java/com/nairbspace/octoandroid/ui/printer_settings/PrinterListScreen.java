package com.nairbspace.octoandroid.ui.printer_settings;

import com.nairbspace.octoandroid.model.PrinterModel;

import java.util.List;

public interface PrinterListScreen {

    void navigateToPrinterDetailsActivity();

    void updateUi(List<PrinterModel> printerModels);
}
