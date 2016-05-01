package com.nairbspace.octoandroid.interactor;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.db.Printer;
import com.nairbspace.octoandroid.data.db.PrinterDao;
import com.nairbspace.octoandroid.data.pref.PrefManager;
import com.nairbspace.octoandroid.net.Connect;
import com.nairbspace.octoandroid.net.Connection;
import com.nairbspace.octoandroid.net.OctoApiImpl;
import com.nairbspace.octoandroid.net.OctoInterceptor;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class GetConnectionImpl implements GetConnection {

    @Inject OctoApiImpl mApi;
    @Inject PrinterDao mPrinterDao;
    @Inject OctoInterceptor mInterceptor;
    @Inject Gson mGson;
    @Inject PrefManager mPrefManager;

    private Printer mPrinter;

    @Inject
    public GetConnectionImpl() {
    }


    @Override
    public void getConnection(final GetConnectionFinishedListener listener) {

        long printerId = mPrefManager.getActivePrinter();

        if (printerId == PrefManager.NO_ACTIVE_PRINTER) {
            listener.onFailure();
            return;
        }

        mPrinter = mPrinterDao.queryBuilder()
                .where(PrinterDao.Properties.Id.eq(printerId))
                .unique();


        String json = mPrinter.getConnection_json();
        Connection connection = mGson.fromJson(json, Connection.class);
        listener.onSuccess(connection);

        syncConnection(listener);
    }

    @Override
    public void syncConnection(final GetConnectionFinishedListener listener) {
        mInterceptor.setInterceptor(mPrinter.getScheme(), mPrinter.getHost(), mPrinter.getPort(), mPrinter.getApi_key());

        listener.onLoading();
        mApi.getConnectionObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Connection>() {
                    @Override
                    public void onCompleted() {
                        listener.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Connection connection) {
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
    public void postConnect(Connect connect) {
        mApi.postConnectObservable(connect)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Connect>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Connected");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Connect connect) {

                    }
                });
    }
}
