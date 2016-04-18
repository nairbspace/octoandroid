package com.nairbspace.octoandroid;

import okhttp3.HttpUrl;

public interface AddPrinterInteractor {

    interface AddPrinterFinishedListener {

        void onSuccess();
    }

    void login(HttpUrl url, String apiKey, AddPrinterFinishedListener listener);
}
