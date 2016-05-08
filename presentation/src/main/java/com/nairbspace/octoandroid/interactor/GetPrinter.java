package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

public interface GetPrinter {

    interface GetPrinterFinishedListener {

        void onLoading();

        void onComplete();

        void onSuccess();

        void onFailure();

        void onSslFailure();

        void onApiKeyFailure();
    }

    void getVersion(PrinterDbEntity printerDbEntity, GetPrinterFinishedListener listener);

    String extractHost(String ipAddress);

    boolean isUrlValid(PrinterDbEntity printerDbEntity);

    int convertPortStringToInt(String port, boolean isSslChecked);

    String convertIsSslCheckedToScheme(boolean isSslChecked);

    PrinterDbEntity setPrinter(PrinterDbEntity printerDbEntity, String accountName, String apiKey,
                               String scheme, String ipAddress, int portNumber);

    void addPrinterToDb(PrinterDbEntity printerDbEntity);

    void deleteOldPrintersInDb(PrinterDbEntity printerDbEntity);

    void addVersionToDb(PrinterDbEntity printerDbEntity, VersionEntity versionEntity);

    void setActivePrinter(long printerId); // TODO will move this interface later
}
