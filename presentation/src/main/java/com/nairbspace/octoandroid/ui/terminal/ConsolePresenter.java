package com.nairbspace.octoandroid.ui.terminal;

import com.nairbspace.octoandroid.ui.templates.Presenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public class ConsolePresenter extends Presenter<ConsoleScreen> {

    private ConsoleScreen mScreen;
    private Subscription mSubscription = Subscriptions.unsubscribed();

    @Inject
    public ConsolePresenter() {
    }

    @Override
    protected void onInitialize(ConsoleScreen consoleScreen) {
        mScreen = consoleScreen;
        mSubscription = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "Recv: ok T:0.93 /0.00 B:0.96 /0.00 @:64 " + aLong;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mScreen.updateUi(s);
                    }
                });
    }

    @Override
    protected void onDestroy(ConsoleScreen consoleScreen) {
        super.onDestroy(consoleScreen);
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
