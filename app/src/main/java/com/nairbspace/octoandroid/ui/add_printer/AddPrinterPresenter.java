package com.nairbspace.octoandroid.ui.add_printer;

public interface AddPrinterPresenter {

    void validateCredentials(String accountName, String ipAddress,
                             String port, String apiKey, boolean isSslChecked);

    void onDestroy();

    void setView(AddPrinterScreen addPrinterScreen);
}
