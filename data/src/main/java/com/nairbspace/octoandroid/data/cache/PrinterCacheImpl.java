package com.nairbspace.octoandroid.data.cache;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.db.PrinterDbEntityDao;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
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
    private final Gson mGson;
    private final AccountManager mAccountManager;

    @Inject
    public PrinterCacheImpl(@NonNull Context context,
                            @NonNull PrinterDbEntityDao printerDbEntityDao,
                            @NonNull ThreadExecutor threadExecutor,
                            @NonNull PrefManager prefManager,
                            @NonNull Gson gson,
                            @NonNull AccountManager accountManager) {
        mContext = context;
        mPrinterDbEntityDao = printerDbEntityDao;
        mThreadExecutor = threadExecutor;
        mPrefManager = prefManager;
        mGson = gson;
        mAccountManager = accountManager;
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

    @Override
    public void put(PrinterDbEntity printerDbEntity, VersionEntity versionEntity) {
        String versionJson = mGson.toJson(versionEntity, VersionEntity.class);
        printerDbEntity.setVersionJson(versionJson);
        printerDbEntity = checkIfPrinterExistsInDb(printerDbEntity);
        mPrinterDbEntityDao.insertOrReplace(printerDbEntity);
        setActivePrinter(printerDbEntity);
        addAccount(printerDbEntity);
    }

    // TODO can probably clean this logic up
    private PrinterDbEntity checkIfPrinterExistsInDb(PrinterDbEntity printerDbEntity) {
        PrinterDbEntity oldPrinterDbEntity = mPrinterDbEntityDao.queryBuilder()
                .where(PrinterDbEntityDao.Properties.Name.eq(printerDbEntity.getName()))
                .unique();
        if (oldPrinterDbEntity != null) {
            printerDbEntity = oldPrinterDbEntity;
        }
        return printerDbEntity;
    }

    private void setActivePrinter(PrinterDbEntity printerDbEntity) {
        mPrefManager.setActivePrinter(printerDbEntity.getId());
    }

    private void addAccount(PrinterDbEntity printerDbEntity) {
        String accountType = "";
        Account account = new Account(printerDbEntity.getName(), validateAccountType(accountType));

        removeAccount(account); // Cannot overwrite, must delete first

        mAccountManager.addAccountExplicitly(account, null, null);
    }

    private void removeAccount(Account account) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mAccountManager.removeAccountExplicitly(account);
        } else {
            //noinspection deprecation
            mAccountManager.removeAccount(account, null, null);
        }
    }

    private String validateAccountType(String accountType) {
        if (TextUtils.isEmpty(accountType)) {
            return "com.nairbspace.actoandroid"; // TODO should add in data res folder
        }
        return accountType;
    }
}
