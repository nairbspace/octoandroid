package com.nairbspace.octoandroid.interactor;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.db.Printer;
import com.nairbspace.octoandroid.data.db.PrinterDao;
import com.nairbspace.octoandroid.data.pref.PrefManager;
import com.nairbspace.octoandroid.net.model.Connect;
import com.nairbspace.octoandroid.net.model.Connection;
import com.nairbspace.octoandroid.net.OctoApiImpl;
import com.nairbspace.octoandroid.net.OctoInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class GetConnectionImpl implements GetConnection {

    @Inject OctoApiImpl mApi;
    @Inject PrinterDao mPrinterDao;
    @Inject OctoInterceptor mInterceptor;
    @Inject Gson mGson;
    @Inject PrefManager mPrefManager;

    private Printer mPrinter;
    private Subscription mPollSubscription;

    @Inject
    public GetConnectionImpl() {
    }


    @Override
    public void getConnectionFromDb(final GetConnectionFinishedListener listener) {

        long printerId = mPrefManager.getActivePrinter();

        if (printerId == PrefManager.NO_ACTIVE_PRINTER) {
            listener.onFailure();
        } else {
            mPrinter = mPrinterDao.queryBuilder()
                    .where(PrinterDao.Properties.Id.eq(printerId))
                    .unique();

            String json = mPrinter.getConnection_json();
            Connection connection = mGson.fromJson(json, Connection.class);
            listener.onSuccess(connection);

            pollConnection(listener);
        }
    }

    @Override
    public void pollConnection(final GetConnectionFinishedListener listener) {
        mInterceptor.setInterceptor(mPrinter.getScheme(), mPrinter.getHost(), mPrinter.getPort(), mPrinter.getApi_key());
        mPollSubscription = Observable.interval(5, TimeUnit.SECONDS)
                .flatMap(new Func1<Long, Observable<Connection>>() {
                    @Override
                    public Observable<Connection> call(Long aLong) {
                        return mApi.getConnectionObservable();
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.d(throwable.toString());
                    }
                })
                .retry()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Connection>() {
                    @Override
                    public void call(Connection connection) {
                        saveConnection(connection);
                        listener.onSuccess(connection);
                    }
                });
    }

    @Override
    public void saveConnection(Connection connection) {
        String json = mGson.toJson(connection);
        mPrinter.setConnection_json(json);
        mPrinterDao.update(mPrinter);
    }

    @Override
    public void postConnect(Connect connect, final GetConnectionFinishedListener listener) {
        listener.onLoading();
        mApi.postConnectObservable(connect)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Connect>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailure();
                    }

                    @Override
                    public void onNext(Connect connect) {

                    }
                });
    }

    @Override
    public void unsubscribePollConnection() {
        if (mPollSubscription != null) {
            mPollSubscription.unsubscribe();
        }
    }
}
