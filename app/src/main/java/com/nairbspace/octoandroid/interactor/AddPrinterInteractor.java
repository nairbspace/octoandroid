package com.nairbspace.octoandroid.interactor;

public interface AddPrinterInteractor {

    interface AddPrinterFinishedListener {

        void onLoading();

        void onComplete();

        void onSuccess();

        void onResponseFailure();

        void onFailure();

        void onSslFailure();
    }

    void login(String scheme, String host, int port, String apiKey, AddPrinterFinishedListener listener);
}
