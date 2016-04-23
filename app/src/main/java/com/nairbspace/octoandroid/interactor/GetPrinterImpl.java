package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.model.OctoAccount;
import com.nairbspace.octoandroid.net.OctoApiImpl;
import com.nairbspace.octoandroid.net.OctoInterceptor;
import com.nairbspace.octoandroid.model.Version;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetPrinterImpl implements GetPrinter {

    private static final String HTTP_SCHEME = "http";
    private static final String HTTPS_SCHEME = "https";

    @Inject OctoApiImpl mApi;
    @Inject OctoInterceptor mInterceptor;

    @Inject
    public GetPrinterImpl() {

    }

    @Override
    public void getVersion(OctoAccount octoAccount, final GetPrinterFinishedListener listener) {
        listener.onLoading();
        mInterceptor.setInterceptor(octoAccount.getScheme(), octoAccount.getHost(),
                octoAccount.getPort(), octoAccount.getApiKey());
        mApi.getVersionObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Version>() {
                    @Override
                    public void onCompleted() {
                        listener.onComplete();
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onComplete();
                        if (e.getMessage().contains("Trust anchor for certification path not found.")) {
                            listener.onSslFailure();
                        } else if (e.getMessage().contains("Invalid API key")) {
                            listener.onApiKeyFailure();
                        } else {
                            listener.onFailure();
                        }
                    }

                    @Override
                    public void onNext(Version version) {

                    }
                });
    }

    @Override
    public String extractHost(String ipAddress) {
        // If user inputted http:// or https:// try to extract only IP Address
        HttpUrl ipAddressUrl = HttpUrl.parse(ipAddress);
        if (ipAddressUrl != null) {
            return ipAddressUrl.host();
        }
        return ipAddress;
    }

    @Override
    public boolean isUrlValid(OctoAccount octoAccount) {
        try {
            new HttpUrl.Builder()
                    .scheme(octoAccount.getScheme())
                    .host(octoAccount.getHost())
                    .port(octoAccount.getPort())
                    .build();
            return true;
        } catch (IllegalArgumentException e) {
            return false;
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
    public String convertIsSslCheckedToScheme(boolean isSslChecked) {
        return isSslChecked ? HTTPS_SCHEME : HTTP_SCHEME;
    }
}