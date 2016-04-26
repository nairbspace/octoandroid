package com.nairbspace.octoandroid.presenter;

import com.nairbspace.octoandroid.ui.AddPrinterScreen;

public interface AddPrinterPresenter {

    void validateCredentials(String accountName, String ipAddress,
                             String port, String apiKey, boolean isSslChecked);

    void onDestroy();

    void setView(AddPrinterScreen addPrinterScreen);
}
