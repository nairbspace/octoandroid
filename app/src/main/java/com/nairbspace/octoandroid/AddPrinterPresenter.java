package com.nairbspace.octoandroid;

public interface AddPrinterPresenter {

    void validateCredentials(String ipAddress, int port, String apiKey);

    int convertPortStringToInt(String port);

    void onDestroy();
}
