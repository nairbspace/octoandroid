package com.nairbspace.octoandroid.interactor;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.db.PrinterDbEntityDao;
import com.nairbspace.octoandroid.data.entity.ConnectEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.disk.PrefHelper;
import com.nairbspace.octoandroid.data.net.OctoApiImplDeprecated;
import com.nairbspace.octoandroid.data.net.OctoInterceptor;

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

    @Inject
    OctoApiImplDeprecated mApi;
    @Inject
    PrinterDbEntityDao mPrinterDbEntityDao;
    @Inject OctoInterceptor mInterceptor;
    @Inject Gson mGson;
    @Inject
    PrefHelper mPrefHelper;

    private PrinterDbEntity mPrinterDbEntity;
    private Subscription mPollSubscription;
    private ConnectionEntity mConnectionEntity;

    @Inject
    public GetConnectionImpl() {
    }


    @Override
    public void getConnection(final GetConnectionFinishedListener listener) {
        if (mPrinterDbEntity != null) {
            mInterceptor.setInterceptor(mPrinterDbEntity.getScheme(), mPrinterDbEntity.getHost(), mPrinterDbEntity.getPort(), mPrinterDbEntity.getApiKey());
            mApi.getConnectionObservable()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ConnectionEntity>() {
                        @Override
                        public void call(ConnectionEntity connectionEntity) {
                            listener.onSuccess(connectionEntity);
                        }
                    });
        }
    }

    @Override
    public void getConnectionFromDb(final GetConnectionFinishedListener listener) {

        long printerId = mPrefHelper.getActivePrinter();

        if (printerId == PrefHelper.NO_ACTIVE_PRINTER) {
            listener.onNoActivePrinter();
        } else {
            mPrinterDbEntity = mPrinterDbEntityDao.queryBuilder()
                    .where(PrinterDbEntityDao.Properties.Id.eq(printerId))
                    .unique();

            if (mPrinterDbEntity != null) {
                String json = mPrinterDbEntity.getConnectionJson();
                ConnectionEntity connectionEntity = mGson.fromJson(json, ConnectionEntity.class);
                if (connectionEntity != null) {
                    listener.onDbSuccess(connectionEntity);
                } else {
                    listener.onDbFailure();
                }
            }
        }
    }

    @Override
    public void pollConnection(final GetConnectionFinishedListener listener) {
        if (mPrinterDbEntity != null && mPollSubscription == null) {
            mPollSubscription = Observable.interval(5, TimeUnit.SECONDS)
                    .flatMap(new Func1<Long, Observable<ConnectionEntity>>() {
                        @Override
                        public Observable<ConnectionEntity> call(Long aLong) {
                            mInterceptor.setInterceptor(mPrinterDbEntity.getScheme(),
                                    mPrinterDbEntity.getHost(), mPrinterDbEntity.getPort(), mPrinterDbEntity.getApiKey());
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
                    .subscribe(new Action1<ConnectionEntity>() {
                        @Override
                        public void call(ConnectionEntity connectionEntity) {
                            saveConnection(connectionEntity);
                            listener.onSuccess(connectionEntity);
                        }
                    });
        }
    }

    @Override
    public void saveConnection(ConnectionEntity connectionEntity) {
        String json = mGson.toJson(connectionEntity);
        mPrinterDbEntity.setConnectionJson(json);
        mPrinterDbEntityDao.update(mPrinterDbEntity);
    }

    @Override
    public void postConnect(final ConnectEntity connectEntity, final GetConnectionFinishedListener listener) {
        listener.onLoading();
        mApi.postConnectObservable(connectEntity)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ConnectEntity>() {
                    @Override
                    public void onCompleted() {
                        listener.onPostComplete(connectEntity);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailure();
                    }

                    @Override
                    public void onNext(ConnectEntity connectEntity) {

                    }
                });
    }

    @Override
    public void unsubscribePollConnection() {
        if (mPollSubscription != null) {
            mPollSubscription.unsubscribe();
            mPollSubscription = null;
        }
    }
}
