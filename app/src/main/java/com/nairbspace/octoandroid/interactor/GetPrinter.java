package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.model.Printer;
import com.nairbspace.octoandroid.model.Version;

public interface GetPrinter {

    interface GetPrinterFinishedListener {

        void onLoading();

        void onComplete();

        void onSuccess();

        void onFailure();

        void onSslFailure();

        void onApiKeyFailure();
    }

    void getVersion(Printer printer, GetPrinterFinishedListener listener);

    String extractHost(String ipAddress);

    boolean isUrlValid(Printer printer);

    int convertPortStringToInt(String port, boolean isSslChecked);

    String convertIsSslCheckedToScheme(boolean isSslChecked);

    Printer setPrinter(Printer printer, String accountName, String apiKey,
                       String scheme, String ipAddress, int portNumber);

    void addPrinterToDb(Printer printer);

    void deleteOldPrintersInDb(Printer printer);

    void addVersionToDb(Printer printer, Version version);
}
