package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.net.OctoApiImpl;
import com.nairbspace.octoandroid.net.OctoInterceptor;
import com.nairbspace.octoandroid.model.Version;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddPrinterInteractorImpl implements AddPrinterInteractor {

    @Inject OctoApiImpl mApi;
    @Inject OctoInterceptor mInterceptor;

    @Inject
    public AddPrinterInteractorImpl() {

    }

    @Override
    public void login(final String scheme, final String host, final int port, final String apiKey, final AddPrinterFinishedListener listener) {
        listener.onLoading();
        mInterceptor.setInterceptor(scheme, host, port, apiKey);
        mApi.getVersionObservable().cache().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Version>() {
                    @Override
                    public void onCompleted() {
                        listener.onComplete();
                        listener.onSuccess(scheme, host, port, apiKey);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onComplete();
                        if (e.getMessage().contains("Trust anchor for certification path not found.")) {
                            listener.onSslFailure();
                        } else {
                            listener.onFailure();
                        }
                    }

                    @Override
                    public void onNext(Version version) {

                    }
                });
    }
}