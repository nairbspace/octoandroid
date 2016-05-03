package com.nairbspace.octoandroid.interactor;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.db.Printer;
import com.nairbspace.octoandroid.data.db.PrinterDao;
import com.nairbspace.octoandroid.data.pref.PrefManager;
import com.nairbspace.octoandroid.net.OctoApiImpl;
import com.nairbspace.octoandroid.net.OctoInterceptor;
import com.nairbspace.octoandroid.net.model.PrinterState;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class GetPrinterStateImpl implements GetPrinterState {

    @Inject PrefManager mPrefManager;
    @Inject PrinterDao mPrinterDao;
    @Inject Gson mGson;
    @Inject OctoInterceptor mInterceptor;
    @Inject OctoApiImpl mApi;
    private Printer mPrinter;
    private Subscription mPollSubscription;

    @Inject
    public GetPrinterStateImpl() {
    }

    @Override
    public void getDataFromDb(GetPrinterStateFinishedListener listener) {
        long id = mPrefManager.getActivePrinter();
        if (id != PrefManager.NO_ACTIVE_PRINTER) {
            mPrinter = mPrinterDao.queryBuilder()
                    .where(PrinterDao.Properties.Id.eq(id))
                    .unique();

            if (mPrinter != null) {
                listener.onSuccessDb(mPrinter);
            }
        }
    }

    @Override
    public <T> T convertJsonToGson(String json, Class<T> type) {
        return mGson.fromJson(json, type);
    }

    public void pollPrinterState(final GetPrinterStateFinishedListener listener) {
        if (mPrinter != null && mPollSubscription == null) {
            mPollSubscription = Observable.interval(5, TimeUnit.SECONDS)
                    .flatMap(new Func1<Long, Observable<PrinterState>>() {
                        @Override
                        public Observable<PrinterState> call(Long aLong) {
                            mInterceptor.setInterceptor(mPrinter.getScheme(),
                                    mPrinter.getHost(), mPrinter.getPort(), mPrinter.getApiKey());
                            return mApi.getPrinterStateObservable();
                        }
                    })
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Timber.d(throwable.toString()); // TODO Need to catch error that represents when the printer is not connected
                        }
                    })
                    .retry()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<PrinterState>() {
                        @Override
                        public void call(PrinterState printerState) {
                            savePrinterState(printerState);
                            listener.onPollSuccess(printerState);
                        }
                    });
        }
    }

    private void savePrinterState(PrinterState printerState) {
        if (mPrinter != null) {
            String printerStateJson = mGson.toJson(printerState);
            mPrinter.setPrinterStateJson(printerStateJson);
            mPrinterDao.update(mPrinter);
        }
    }

    public void unsubscribePollSubscription() {
        if (mPollSubscription != null) {
            mPollSubscription.unsubscribe();
            mPollSubscription = null;
        }
    }
}
