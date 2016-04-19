package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.net.OctoApi;
import com.nairbspace.octoandroid.net.OctoInterceptor;
import com.nairbspace.octoandroid.net.Version;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class AddPrinterInteractorImpl implements AddPrinterInteractor {

    @Inject OctoApi mApi;
    @Inject OctoInterceptor mInterceptor;

    @Inject
    public AddPrinterInteractorImpl() {

    }

    @Override
    public void login(String scheme, String host, int port, String apiKey, final AddPrinterFinishedListener listener) {
        listener.onLoading();
        mInterceptor.setInterceptor(scheme, host, port, apiKey);
        Call<Version> call = mApi.getVersion();
        call.enqueue(new Callback<Version>() {
            @Override
            public void onResponse(Call<Version> call, Response<Version> response) {
                listener.onComplete();
                if (response.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onResponseFailure();
                }
            }

            @Override
            public void onFailure(Call<Version> call, Throwable t) {
                listener.onComplete();
                if (t.getMessage().contains("Trust anchor for certification path not found.")) {
                    Timber.d(t.getMessage());
                    listener.onSslFailure();
                } else {
                    listener.onFailure();
                }
            }
        });
    }
}