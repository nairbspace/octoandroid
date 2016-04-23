package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.model.OctoAccount;

public interface GetPrinter {

    interface GetPrinterFinishedListener {

        void onLoading();

        void onComplete();

        void onSuccess();

        void onFailure();

        void onSslFailure();

        void onApiKeyFailure();
    }

    void getVersion(OctoAccount octoAccount, GetPrinterFinishedListener listener);

    String extractHost(String ipAddress);

    boolean isUrlValid(OctoAccount octoAccount);

    int convertPortStringToInt(String port, boolean isSslChecked);

    String convertIsSslCheckedToScheme(boolean isSslChecked);
}
