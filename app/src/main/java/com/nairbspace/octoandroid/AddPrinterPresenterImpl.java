package com.nairbspace.octoandroid;

import android.text.TextUtils;

import okhttp3.HttpUrl;

public class AddPrinterPresenterImpl implements AddPrinterPresenter, AddPrinterInteractor.AddPrinterFinishedListener {
    private AddPrinterScreen mAddPrinterScreen;
    private AddPrinterInteractor mAddPrinterInteractor;

    public AddPrinterPresenterImpl(AddPrinterScreen addPrinterScreen) {
        mAddPrinterScreen = addPrinterScreen;
        mAddPrinterInteractor = new AddPrinterInteractorImpl();
    }

    @Override
    public void validateCredentials(String ipAddress, int port, String apiKey) {

        if (TextUtils.isEmpty(ipAddress)) {
            mAddPrinterScreen.showIpAddressError("IP Address cannot be blank");
            return;
        }

        // If user inputted http:// or https:// try to extract only IP Address
        if (ipAddress.contains("://")) {
            String[] split = ipAddress.split("://");
            if (split.length > 0) {
                ipAddress = split[1];
            }
        }

        try {
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https") // Try secure connection first
                    .host(ipAddress)
                    .port(port)
                    .build();
            mAddPrinterInteractor.login(url, apiKey, this);
        } catch (IllegalArgumentException e) {
            mAddPrinterScreen.showIpAddressError("Incorrect formatting");
        }
    }

    @Override
    public int convertPortStringToInt(String port) {
        int formattedPortNum;
        try {
            formattedPortNum = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            formattedPortNum = 443; // Set default port to 443
        }

        return formattedPortNum;
    }

    @Override
    public void onDestroy() {
        mAddPrinterScreen = null;
    }

    @Override
    public void onSuccess() {
    }
}
