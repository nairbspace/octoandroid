package com.nairbspace.octoandroid.data.disk;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
import com.nairbspace.octoandroid.data.exception.ErrorSavingException;
import com.nairbspace.octoandroid.data.exception.NoActivePrinterException;
import com.nairbspace.octoandroid.data.exception.PrinterDataNotFoundException;
import com.nairbspace.octoandroid.data.mapper.EntityMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;

@Singleton
public class DiskManagerImpl implements DiskManager {
    private static final int EXPIRATION_TIME = 10 * 60 * 1000; // in milliseconds

    private final PrefHelper mPrefHelper;
    private final AccountHelper mAccountHelper;
    private final DbHelper mDbHelper;
    private final EntityMapper mMapper;

    @Inject
    public DiskManagerImpl(PrefHelper prefHelper, AccountHelper accountHelper,
                           DbHelper dbHelper, EntityMapper mapper) {
        mPrefHelper = prefHelper;
        mAccountHelper = accountHelper;
        mDbHelper = dbHelper;
        mMapper = mapper;
    }

    @Override
    public Observable<PrinterDbEntity> get() {
        return Observable.create(new Observable.OnSubscribe<PrinterDbEntity>() {
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
                if (printerDbEntity!=null && printerDbEntity.getVersionJson() == null) {
                    // TODO need better way to delete data
                    mDbHelper.deleteOldPrinterInDb(printerDbEntity);
                    mAccountHelper.removeAccount(printerDbEntity);
                    mPrefHelper.setActivePrinter(PrefHelper.NO_ACTIVE_PRINTER);
                    subscriber.onError(new PrinterDataNotFoundException());
                }

                if (!mAccountHelper.doesPrinterExistInAccountManager(printerDbEntity)) {
                    mAccountHelper.addAccount(printerDbEntity);
                }

                subscriber.onNext(printerDbEntity);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<PrinterDbEntity> get(final String name) {
        return Observable.create(new Observable.OnSubscribe<PrinterDbEntity>() {
            @Override
            public void call(Subscriber<? super PrinterDbEntity> subscriber) {
                PrinterDbEntity printerDbEntity = mDbHelper.getPrinterFromDbByName(name);
                if (printerDbEntity == null) {
                    subscriber.onError(new PrinterDataNotFoundException());
                }
                subscriber.onNext(printerDbEntity);
                subscriber.onCompleted();
            }
        });
    }

    private void put(VersionEntity versionEntity) {
        PrinterDbEntity printerDbEntity = mDbHelper.getActivePrinterDbEntity();
        String versionJson = mMapper.serialize(versionEntity);
        printerDbEntity.setVersionJson(versionJson);
        mDbHelper.insertOrReplace(printerDbEntity);
        mPrefHelper.setSaveTimeMillis(System.currentTimeMillis());
    }

    private void put(PrinterDbEntity printerDbEntity) {
        mDbHelper.deleteOldPrinterInDb(printerDbEntity);
        mDbHelper.insertOrReplace(printerDbEntity);
        mPrefHelper.setActivePrinter(printerDbEntity.getId());
        mPrefHelper.setSaveTimeMillis(System.currentTimeMillis());
        mAccountHelper.addAccount(printerDbEntity);
    }

    @Override
    public Action1<PrinterDbEntity> putPrinterDbEntity() {
        return new Action1<PrinterDbEntity>() {
            @Override
            public void call(PrinterDbEntity printerDbEntity) {
                try {
                    put(printerDbEntity);
                } catch (Exception e) {
                    throw Exceptions.propagate(new ErrorSavingException());
                }
            }
        };
    }

    @Override
    public Observable<Boolean> putVersionEntity(Observable<VersionEntity> versionEntityObs) {
        return versionEntityObs.map(new Func1<VersionEntity, Boolean>() {
            @Override
            public Boolean call(VersionEntity versionEntity) {
                try {
                    put(versionEntity);
                    return true;
                } catch (Exception e) {
                    throw Exceptions.propagate(new ErrorSavingException(e));
                }
            }
        });
    }

    @Override
    public Observable<Boolean> deleteOldPrinterInDbObs(Observable<PrinterDbEntity> printerDbEntityObs) {
        return printerDbEntityObs.map(new Func1<PrinterDbEntity, Boolean>() {
            @Override
            public Boolean call(PrinterDbEntity printerDbEntity) {
                PrinterDbEntity oldPrinterDbEntity = mDbHelper
                        .getPrinterFromDbByName(printerDbEntity.getName());

                if (oldPrinterDbEntity == null) {
                    throw Exceptions.propagate(new PrinterDataNotFoundException());
                }

                if (mPrefHelper.isPrinterActive(oldPrinterDbEntity)) {
                    //TODO need to handle this when there's multiple printers!!
                    mPrefHelper.setActivePrinter(PrefHelper.NO_ACTIVE_PRINTER);
                }

                mDbHelper.deleteOldPrinterInDb(oldPrinterDbEntity);
                return true;
            }
        });
    }

    @Override
    public Action1<Throwable> deleteUnverifiedPrinter() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                PrinterDbEntity printerDbEntity = mDbHelper.getActivePrinterDbEntity();
                mDbHelper.deleteOldPrinterInDb(printerDbEntity);
                mAccountHelper.removeAccount(printerDbEntity);
                mPrefHelper.setActivePrinter(PrefHelper.NO_ACTIVE_PRINTER);
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
    public Observable<ConnectionEntity> getConnection() {
        return get().map(new Func1<PrinterDbEntity, ConnectionEntity>() {
            @Override
            public ConnectionEntity call(PrinterDbEntity printerDbEntity) {
                try {
                    String json = printerDbEntity.getConnectionJson();
                    return mMapper.deserialize(json, ConnectionEntity.class);
                } catch (Exception e) {
                    throw Exceptions.propagate(new PrinterDataNotFoundException(e));
                }
            }
        });
    }



    @Override
    public Observable<VersionEntity> getVersion() {
        return get().map(new Func1<PrinterDbEntity, VersionEntity>() {
            @Override
            public VersionEntity call(PrinterDbEntity printerDbEntity) {
                try {
                    String json = printerDbEntity.getVersionJson();
                    return mMapper.deserialize(json, VersionEntity.class);
                } catch (Exception e) {
                    throw Exceptions.propagate(new PrinterDataNotFoundException());
                }
            }
        });
    }
}
