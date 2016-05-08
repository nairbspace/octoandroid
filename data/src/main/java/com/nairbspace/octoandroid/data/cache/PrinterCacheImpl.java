package com.nairbspace.octoandroid.data.cache;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.db.PrinterDbEntityDao;
import com.nairbspace.octoandroid.data.exception.NoActivePrinterException;
import com.nairbspace.octoandroid.data.exception.PrinterDataNotFoundException;
import com.nairbspace.octoandroid.data.pref.PrefManager;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

@Singleton
public class PrinterCacheImpl implements PrinterCache {

    private final Context mContext;
    private final PrinterDbEntityDao mPrinterDbEntityDao;
    private final ThreadExecutor mThreadExecutor;
    private final PrefManager mPrefManager;

    @Inject
    public PrinterCacheImpl(@NonNull Context context,
                            @NonNull PrinterDbEntityDao printerDbEntityDao,
                            @NonNull ThreadExecutor threadExecutor,
                            @NonNull PrefManager prefManager) {
        mContext = context;
        mPrinterDbEntityDao = printerDbEntityDao;
        mThreadExecutor = threadExecutor;
        mPrefManager = prefManager;
    }


    @Override
    public Observable<PrinterDbEntity> get() {
        return Observable.create(new Observable.OnSubscribe<PrinterDbEntity>() {
            @Override
            public void call(Subscriber<? super PrinterDbEntity> subscriber) {
                long printerId = mPrefManager.getActivePrinter();
                if (printerId == PrefManager.NO_ACTIVE_PRINTER) {
                    subscriber.onError(new NoActivePrinterException());
                } else {
                    PrinterDbEntity printerDbEntity = mPrinterDbEntityDao.queryBuilder()
                            .where(PrinterDbEntityDao.Properties.Id.eq(printerId))
                            .unique();

                    if (printerDbEntity != null) {
                        subscriber.onNext(printerDbEntity);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new PrinterDataNotFoundException());
                    }

                }

            }
        });
    }
}
