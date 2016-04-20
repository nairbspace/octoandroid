package com.nairbspace.octoandroid.presenter;

import com.nairbspace.octoandroid.interactor.AddPrinterInteractor;
import com.nairbspace.octoandroid.interactor.AddPrinterInteractorImpl;
import com.nairbspace.octoandroid.ui.AddPrinterScreen;

import javax.inject.Inject;

import okhttp3.HttpUrl;

public class AddPrinterPresenterImpl implements AddPrinterPresenter, AddPrinterInteractor.AddPrinterFinishedListener {

    private static final String HTTP_SCHEME = "http";
    private static final String HTTPS_SCHEME = "https";

    private AddPrinterScreen mAddPrinterScreen;
    @Inject AddPrinterInteractorImpl mAddPrinterInteractor;

    @Inject
    public AddPrinterPresenterImpl() {
    }

    @Override
    public void validateCredentials(boolean isSslChecked, String ipAddress, int port, String apiKey) {

        if (ipAddress == null || ipAddress.isEmpty()) {
            mAddPrinterScreen.showIpAddressError("IP Address cannot be blank");
            return;
        }

        String scheme = isSslChecked ? HTTPS_SCHEME : HTTP_SCHEME;

        // If user inputted http:// or https:// try to extract only IP Address
        if (ipAddress.contains("://")) {
            String[] split = ipAddress.split("://");
            if (split.length > 0) {
                ipAddress = split[1];
            }
        }

        if (apiKey == null) {
            apiKey = "";
        }

        try {
            HttpUrl url = new HttpUrl.Builder()
                    .scheme(scheme)
                    .host(ipAddress)
                    .port(port)
                    .build();
            mAddPrinterInteractor.login(scheme, ipAddress, port, apiKey, this);
        } catch (IllegalArgumentException e) {
            mAddPrinterScreen.showIpAddressError("Incorrect formatting");
        }
    }

    @Override
    public int convertPortStringToInt(String port, boolean isSslChecked) {
        int formattedPortNum;
        try {
            formattedPortNum = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            if (isSslChecked) {
                formattedPortNum = 443;
            } else {
                formattedPortNum = 80;
            }
        }

        return formattedPortNum;
    }

    @Override
    public void onDestroy() {
        mAddPrinterScreen = null;
    }

    @Override
    public void setView(AddPrinterScreen addPrinterScreen) {
        mAddPrinterScreen = addPrinterScreen;
    }

    @Override
    public void onLoading() {
        mAddPrinterScreen.hideSoftKeyboard(true);
        mAddPrinterScreen.showProgress(true);
    }

    @Override
    public void onComplete() {
        mAddPrinterScreen.showProgress(false);
    }

    @Override
    public void onSuccess(String scheme, String host, int port, String apiKey) {
        mAddPrinterScreen.showSnackbar("Success");
        mAddPrinterScreen.addAccount(scheme, host, port, apiKey);
    }

    @Override
    public void onResponseFailure() {
        mAddPrinterScreen.showSnackbar("Failure");
    }

    @Override
    public void onFailure() {
        mAddPrinterScreen.showSnackbar("Failure");
    }

    @Override
    public void onSslFailure() {
        mAddPrinterScreen.showAlertDialog("SSL Error",
                "SSL Certificate is not signed. If accessing printer locally try unsecure connection.");
    }
}
