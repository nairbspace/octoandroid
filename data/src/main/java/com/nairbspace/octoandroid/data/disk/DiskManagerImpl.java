package com.nairbspace.octoandroid.data.disk;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
import com.nairbspace.octoandroid.data.exception.ErrorSavingException;
import com.nairbspace.octoandroid.data.exception.NoActivePrinterException;
import com.nairbspace.octoandroid.data.exception.PrinterDataNotFoundException;
import com.nairbspace.octoandroid.data.mapper.EntitySerializer;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;

@Singleton
public class DiskManagerImpl implements DiskManager {
    private static final int EXPIRATION_TIME = 2 * 60 * 1000; // in milliseconds

    private final PrefHelper mPrefHelper;
    private final AccountHelper mAccountHelper;
    private final DbHelper mDbHelper;
    private final EntitySerializer mEntitySerializer;
    private final ResManager mResManager;

    @Inject
    public DiskManagerImpl(PrefHelper prefHelper, AccountHelper accountHelper,
                           DbHelper dbHelper, EntitySerializer entitySerializer,
                           ResManager resManager) {
        mPrefHelper = prefHelper;
        mAccountHelper = accountHelper;
        mDbHelper = dbHelper;
        mEntitySerializer = entitySerializer;
        mResManager = resManager;
    }

    @Override
    public Observable.OnSubscribe<PrinterDbEntity> getPrinterInDb() {
        return new Observable.OnSubscribe<PrinterDbEntity>() {
            @Override
            public void call(Subscriber<? super PrinterDbEntity> subscriber) {
                if (!mPrefHelper.doesActivePrinterExist()) {
                    subscriber.onError(new NoActivePrinterException());
                }

                PrinterDbEntity printerDbEntity = mDbHelper.getActivePrinterDbEntity();
                if (printerDbEntity == null) {
                    subscriber.onError(new PrinterDataNotFoundException());
                }

                // If version info is null that means never verified connection
                if (printerDbEntity != null && printerDbEntity.getVersionJson() == null) {
                    // TODO need better way to delete data
                    mDbHelper.deletePrinterInDb(printerDbEntity);
                    mAccountHelper.removeAccount(printerDbEntity);
                    mPrefHelper.setActivePrinter(mResManager.getNoActivePrinterValue());
                    subscriber.onError(new PrinterDataNotFoundException());
                }

                if (!mAccountHelper.doesPrinterExistInAccountManager(printerDbEntity)) {
                    mAccountHelper.addAccount(printerDbEntity);
                }

                subscriber.onNext(printerDbEntity);
                subscriber.onCompleted();
            }
        };
    }

    @Override
    public Observable.OnSubscribe<PrinterDbEntity> getPrinterByName(final String name) {
        return new Observable.OnSubscribe<PrinterDbEntity>() {
            @Override
            public void call(Subscriber<? super PrinterDbEntity> subscriber) {
                PrinterDbEntity printerDbEntity = mDbHelper.getPrinterFromDbByName(name);
                if (printerDbEntity == null) {
                    subscriber.onError(new PrinterDataNotFoundException());
                }
                subscriber.onNext(printerDbEntity);
                subscriber.onCompleted();
            }
        };
    }

    @Override
    public Action1<PrinterDbEntity> putPrinterInDb() {
        return new Action1<PrinterDbEntity>() {
            @Override
            public void call(PrinterDbEntity printerDbEntity) {
                try {
                    mDbHelper.deletePrinterInDb(printerDbEntity);
                    mDbHelper.insertOrReplace(printerDbEntity);
                    mPrefHelper.setActivePrinter(printerDbEntity.getId());
                    mPrefHelper.setSaveTimeMillis(System.currentTimeMillis());
                    mAccountHelper.addAccount(printerDbEntity);
                } catch (Exception e) {
                    throw Exceptions.propagate(new ErrorSavingException());
                }
            }
        };
    }

    @Override
    public Action1<VersionEntity> putVersionInDb() {
        return new Action1<VersionEntity>() {
            @Override
            public void call(VersionEntity versionEntity) {
                try {
                    PrinterDbEntity printerDbEntity = mDbHelper.getActivePrinterDbEntity();
                    String versionJson = mEntitySerializer.serialize(versionEntity);
                    printerDbEntity.setVersionJson(versionJson);
                    mDbHelper.insertOrReplace(printerDbEntity);
                } catch (Exception e) {
                    throw Exceptions.propagate(new ErrorSavingException(e));
                }
            }
        };
    }

    @Override
    public Action1<ConnectionEntity> putConnectionInDb() {
        return new Action1<ConnectionEntity>() {
            @Override
            public void call(ConnectionEntity connectionEntity) {
                try {
                    PrinterDbEntity printerDbEntity = mDbHelper.getActivePrinterDbEntity();
                    String connectionJson = mEntitySerializer.serialize(connectionEntity);
                    printerDbEntity.setConnectionJson(connectionJson);
                    mDbHelper.insertOrReplace(printerDbEntity);
                } catch (Exception e) {
                    throw Exceptions.propagate(new ErrorSavingException());
                }
            }
        };
    }

    @Override
    public Func1<PrinterDbEntity, Boolean> deletePrinterByName() {
        return new Func1<PrinterDbEntity, Boolean>() {
            @Override
            public Boolean call(PrinterDbEntity printerDbEntity) {
                PrinterDbEntity oldPrinterDbEntity = mDbHelper
                        .getPrinterFromDbByName(printerDbEntity.getName());

                if (oldPrinterDbEntity == null) {
                    throw Exceptions.propagate(new PrinterDataNotFoundException());
                }

                if (mPrefHelper.isPrinterActive(oldPrinterDbEntity)) {
                    //TODO need to handle this when there's multiple printers!!
                    mPrefHelper.setActivePrinter(mResManager.getNoActivePrinterValue());
                }

                mDbHelper.deletePrinterInDb(oldPrinterDbEntity);
                return true;
            }
        };
    }

    @Override
    public Action1<Throwable> deleteUnverifiedPrinter() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                try {
                    PrinterDbEntity printerDbEntity = mDbHelper.getActivePrinterDbEntity();
                    mDbHelper.deletePrinterInDb(printerDbEntity);
                    mAccountHelper.removeAccount(printerDbEntity);
                    mPrefHelper.setActivePrinter(mResManager.getNoActivePrinterValue());
                } catch (Exception e) {
                    throw Exceptions.propagate(new PrinterDataNotFoundException(e));
                }
            }
        };
    }

    @Override
    public boolean isSaved() {
        long printerId = mPrefHelper.getActivePrinter();
        PrinterDbEntity printerDbEntity = mDbHelper.getPrinterFromDbById(printerId);
        if (printerDbEntity == null) {
            return false;
        }

        if (printerDbEntity.getVersionJson() == null) {
            return false;
        }

        //noinspection RedundantIfStatement
        if (printerDbEntity.getConnectionJson() == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = mPrefHelper.getLastSaveTimeMillis();

        return ((currentTime - lastUpdateTime) > EXPIRATION_TIME);
    }

    @Override
    public Func1<PrinterDbEntity, ConnectionEntity> getConnectionInDb() {
        return new Func1<PrinterDbEntity, ConnectionEntity>() {
            @Override
            public ConnectionEntity call(PrinterDbEntity printerDbEntity) {
                try {
                    String json = printerDbEntity.getConnectionJson();
                    ConnectionEntity connectionEntity = mEntitySerializer
                            .deserialize(json, ConnectionEntity.class);
                    if (connectionEntity != null) {
                        return connectionEntity;
                    } else {
                        throw Exceptions.propagate(new PrinterDataNotFoundException());
                    }
                } catch (Exception e) {
                    throw Exceptions.propagate(new PrinterDataNotFoundException(e));
                }
            }
        };
    }
}
