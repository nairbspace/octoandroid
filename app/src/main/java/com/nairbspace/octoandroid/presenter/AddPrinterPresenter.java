package com.nairbspace.octoandroid.presenter;

import com.nairbspace.octoandroid.ui.AddPrinterScreen;

public interface AddPrinterPresenter {

    void validateCredentials(boolean isSslChecked, String ipAddress, int port, String apiKey);

    int convertPortStringToInt(String port, boolean isSslChecked);

    void onDestroy();

    void setView(AddPrinterScreen addPrinterScreen);
}
