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

import de.greenrobot.dao.DaoException;
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
                    PrinterDbEntity printerDbEntity = mPrinterDbEntityDao.queryBuilder() // TODO Try block
                            .where(PrinterDbEntityDao.Properties.Id.eq(printerId))
                            .unique();

                    if (printerDbEntity != null) {
                        if (!doesPrinterExistInAccountManager(printerDbEntity)) {
                            addAccount(printerDbEntity);
                        }
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
    public Observable<PrinterDbEntity> get(final String name) {
        return Observable.create(new Observable.OnSubscribe<PrinterDbEntity>() {
            @Override
            public void call(Subscriber<? super PrinterDbEntity> subscriber) {
                PrinterDbEntity printerDbEntity;
                try {
                    printerDbEntity = mPrinterDbEntityDao.queryBuilder()
                            .where(PrinterDbEntityDao.Properties.Name.eq(name))
                            .unique();
                } catch (DaoException e) {
                    printerDbEntity = null;
                    subscriber.onError(new PrinterDataNotFoundException());
                }

                if (printerDbEntity != null) {
                    subscriber.onNext(printerDbEntity);
                    subscriber.onCompleted();
                }
            }
        });
    }

    private boolean doesPrinterExistInAccountManager(PrinterDbEntity printerDbEntity) {
        Account[] accounts = mAccountManager.getAccounts();
        if (accounts.length == 0) {
            return false;
        }

        for (Account account : accounts) {
            if (account.name.equals(printerDbEntity.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void put(PrinterDbEntity printerDbEntity, VersionEntity versionEntity) {
        String versionJson = mGson.toJson(versionEntity, VersionEntity.class);
        printerDbEntity.setVersionJson(versionJson);
        deleteOldPrinterInDb(printerDbEntity);
        mPrinterDbEntityDao.insertOrReplace(printerDbEntity);
        setActivePrinter(printerDbEntity);
        addAccount(printerDbEntity);
    }

    @Override
    public Observable<Boolean> deleteOldPrinterInDbObservable(final PrinterDbEntity printerDbEntity) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                PrinterDbEntity oldPrinterDbEntity;
                try {
                    oldPrinterDbEntity = mPrinterDbEntityDao.queryBuilder()
                            .where(PrinterDbEntityDao.Properties.Name.eq(printerDbEntity.getName()))
                            .unique();
                } catch (DaoException e) {
                    subscriber.onError(new PrinterDataNotFoundException());
                    oldPrinterDbEntity = null;
                }

                if (oldPrinterDbEntity != null) {
                    if (oldPrinterDbEntity.getId() == mPrefManager.getActivePrinter()) {
                        //TODO need to handle this when there's multiple printers!!
                        mPrefManager.setActivePrinter(PrefManager.NO_ACTIVE_PRINTER);
                    }
                    mPrinterDbEntityDao.delete(oldPrinterDbEntity);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }
            }
        });
    }

    // TODO combine with code above
    private void deleteOldPrinterInDb(PrinterDbEntity printerDbEntity) {
        PrinterDbEntity oldPrinterDbEntity;
        try {
            oldPrinterDbEntity = mPrinterDbEntityDao.queryBuilder()
                    .where(PrinterDbEntityDao.Properties.Name.eq(printerDbEntity.getName()))
                    .unique();
        } catch (DaoException e) {
            oldPrinterDbEntity = null;
        }

        if (oldPrinterDbEntity != null) {
            mPrinterDbEntityDao.delete(oldPrinterDbEntity);
        }
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
