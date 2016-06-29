package com.nairbspace.octoandroid.data.disk;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
import com.nairbspace.octoandroid.data.exception.ErrorSavingException;
import com.nairbspace.octoandroid.data.exception.NoActivePrinterException;
import com.nairbspace.octoandroid.data.exception.PrinterDataNotFoundException;
import com.nairbspace.octoandroid.data.exception.PrinterNameNotUniqueException;
import com.nairbspace.octoandroid.data.mapper.EntitySerializer;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

@Singleton
public class DiskManagerImpl implements DiskManager {
    private static final int EXPIRATION_TIME = 2 * 60 * 1000; // in milliseconds

    private final PrefHelper mPrefHelper;
    private final AccountHelper mAccountHelper;
    private final DbHelper mDbHelper;
    private final EntitySerializer mEntitySerializer;

    @Inject
    public DiskManagerImpl(PrefHelper prefHelper, AccountHelper accountHelper,
                           DbHelper dbHelper, EntitySerializer entitySerializer) {
        mPrefHelper = prefHelper;
        mAccountHelper = accountHelper;
        mDbHelper = dbHelper;
        mEntitySerializer = entitySerializer;
    }

    @Override
    public boolean isDbEmpty() {
        List<PrinterDbEntity> printerDbEntities = mDbHelper.getPrintersFromDb();
        return printerDbEntities == null || printerDbEntities.isEmpty();
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

                subscriber.onNext(printerDbEntity);
                subscriber.onCompleted();
            }
        };
    }

    @Override
    public Observable.OnSubscribe<List<PrinterDbEntity>> getPrintersInDb() {
        return new Observable.OnSubscribe<List<PrinterDbEntity>>() {
            @Override
            public void call(Subscriber<? super List<PrinterDbEntity>> subscriber) {
                List<PrinterDbEntity> list = mDbHelper.getPrintersFromDb();
                if (list == null) {
                    subscriber.onError(new PrinterDataNotFoundException());
                }
                subscriber.onNext(list);
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
    public Observable.OnSubscribe<PrinterDbEntity> getPrinterById(final long id) {
        return new Observable.OnSubscribe<PrinterDbEntity>() {
            @Override
            public void call(Subscriber<? super PrinterDbEntity> subscriber) {
                PrinterDbEntity printerDbEntity = mDbHelper.getPrinterFromDbById(id);
                if (printerDbEntity == null) {
                    subscriber.onError(new PrinterDataNotFoundException());
                }

                subscriber.onNext(printerDbEntity);
                subscriber.onCompleted();
            }
        };
    }

    @Override
    public Func1<PrinterDbEntity, PrinterDbEntity> putPrinterInDb() {
        return new Func1<PrinterDbEntity, PrinterDbEntity>() {
            @Override
            public PrinterDbEntity call(PrinterDbEntity printerDbEntity) {
                try {
                    return syncAddPrinter(printerDbEntity);
                } catch (Exception e) {
                    throw Exceptions.propagate(new ErrorSavingException());
                }
            }
        };
    }

    /**
     * @param printerDbEntity the entity to be added to db, account manager, and active printer
     * @return the entity with an id
     */
    private PrinterDbEntity syncAddPrinter(PrinterDbEntity printerDbEntity) {
        mAccountHelper.removeAccount(printerDbEntity); // This calls syncDbAndAccountDeletion()
        long id = mDbHelper.insertOrReplace(printerDbEntity);

        // Need to fetch printer from db which has id
        printerDbEntity = mDbHelper.getPrinterFromDbById(id);

        mPrefHelper.setActivePrinter(printerDbEntity.getId());
        mAccountHelper.addAccount(printerDbEntity);
        return printerDbEntity;
    }

    /**
     * @param printerDbEntity the entity to be deleted in db, account manager, and if active printer
     * @return the result of it was successful
     */
    private boolean syncDeletePrinter(PrinterDbEntity printerDbEntity) {
        mAccountHelper.removeAccount(printerDbEntity); // This calls syncDbAndAccountDeletion()
        mDbHelper.deletePrinterInDb(printerDbEntity);
        if (printerDbEntity.getId() == null) return false;
        long id = printerDbEntity.getId();
        if (mPrefHelper.isPrinterActive(id)) mPrefHelper.resetActivePrinter();
        return true;
    }

    @Override
    public Func1<PrinterDbEntity, PrinterDbEntity> putPrinterInPrefs() {
        return new Func1<PrinterDbEntity, PrinterDbEntity>() {
            @Override
            public PrinterDbEntity call(PrinterDbEntity printerDbEntity) {
                try {
                    mPrefHelper.setPrinterDbEntityToPrefs(printerDbEntity);
                    return printerDbEntity;
                } catch (Exception e) {
                    throw Exceptions.propagate(new ErrorSavingException());
                }
            }
        };
    }

    @Override
    public Func1<VersionEntity, VersionEntity> putVersionInDb() {
        return new Func1<VersionEntity, VersionEntity>() {
            @Override
            public VersionEntity call(VersionEntity versionEntity) {
                try {
                    PrinterDbEntity printerDbEntity = mDbHelper.getActivePrinterDbEntity();
                    String versionJson = mEntitySerializer.serialize(versionEntity);
                    printerDbEntity.setVersionJson(versionJson);
                    mDbHelper.insertOrReplace(printerDbEntity);
                    return versionEntity;
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

    /**
     * This is used to sync the database when deleting from account manager.
     */
    @Override
    public Func1<PrinterDbEntity, Boolean> syncDbAndAccountDeletion() {
        return new Func1<PrinterDbEntity, Boolean>() {
            @Override
            public Boolean call(PrinterDbEntity printerDbEntity) {
                PrinterDbEntity old = mDbHelper.getPrinterFromDbByName(printerDbEntity.getName());
                if (old == null) {
                    throw Exceptions.propagate(new PrinterDataNotFoundException());
                }

                // DO NOT CALL any methods related to deleting from account or you will infinite loop!
                // ie: mAccountManager.removeAccount()

                if (mPrefHelper.isPrinterActive(old)) mPrefHelper.resetActivePrinter();
                mDbHelper.deletePrinterInDb(old);
                return true;
            }
        };
    }

    @Override
    public Func1<PrinterDbEntity, Boolean> deletePrinterById() {
        return new Func1<PrinterDbEntity, Boolean>() {
            @Override
            public Boolean call(PrinterDbEntity printerDbEntity) {
                try {
                    syncDeletePrinter(printerDbEntity);
                    return true;
                } catch (Exception e) {
                    throw Exceptions.propagate(e);
                }
            }
        };
    }

    @Override
    public Action1<Throwable> deleteUnverifiedPrinter(final long previousActiveId) {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                try {
                    PrinterDbEntity printerDbEntity = mDbHelper.getActivePrinterDbEntity();
                    syncDeletePrinter(printerDbEntity);
                    mPrefHelper.setActivePrinter(previousActiveId);
                } catch (Exception e) {
                    throw Exceptions.propagate(new PrinterDataNotFoundException(e));
                }
            }
        };
    }

    @Override
    public boolean isSaved() {
        long printerId = mPrefHelper.getActivePrinterId();
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
                    ConnectionEntity entity = mEntitySerializer.deserialize(json, ConnectionEntity.class);
                    if (entity == null) throw Exceptions.propagate(new PrinterDataNotFoundException());
                    return entity;
                } catch (Exception e) {
                    throw Exceptions.propagate(new PrinterDataNotFoundException(e));
                }
            }
        };
    }

    @Override
    public boolean isPushNotificationOn() {
        return mPrefHelper.isPushNotificationOn();
    }

    @Override
    public boolean isStickyNotificationOn() {
        return mPrefHelper.isStickyNotificationOn();
    }

    @Override
    public long setActivePrinter(long id) {
        mPrefHelper.setActivePrinter(id);
        return id;
    }

    @Override
    public long getActivePrinterId() {
        return mPrefHelper.getActivePrinterId();
    }

    @Override
    public PrinterDbEntity getPrinterByEditPrefId() {
        long id = mPrefHelper.getEditPrefsId();
        return mDbHelper.getPrinterFromDbById(id);
    }

    @Override
    public Observable putEditPrinterDbEntityInDb() {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    PrinterDbEntity entity = mPrefHelper.getEditPrinterDbEntity();
                    if (mDbHelper.doesPrinterNameExist(entity)) {
                        subscriber.onError(new PrinterNameNotUniqueException());
                    }

                    // Need to delete from AccountManager or will get multiple accounts
                    PrinterDbEntity old = getPrinterByEditPrefId();
                    syncDeletePrinter(old);

                    syncAddPrinter(entity);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Action1<Throwable> deleteFailedEdit(final PrinterDbEntity oldEntity, final long previousActiveId) {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                syncAddPrinter(oldEntity);
                mPrefHelper.setActivePrinter(previousActiveId);
            }
        };
    }

    @Override
    public Action0 resetActivePrinter(final long previousActiveId) {
        return new Action0() {
            @Override
            public void call() {
                mPrefHelper.setActivePrinter(previousActiveId);
            }
        };
    }
}
