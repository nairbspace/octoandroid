package com.nairbspace.octoandroid.domain.interactor;

public class DefaultSubscriber<T> extends rx.Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(T t) {

    }
}