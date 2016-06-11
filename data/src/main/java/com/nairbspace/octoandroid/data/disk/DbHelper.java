package com.nairbspace.octoandroid.data.disk;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.db.PrinterDbEntityDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/** Convenience methods for db related info */
@Singleton
public class DbHelper {

    private final PrinterDbEntityDao mPrinterDbEntityDao;
    private final PrefHelper mPrefHelper;

    @Inject
    public DbHelper(PrinterDbEntityDao printerDbEntityDao, PrefHelper prefHelper) {
        mPrinterDbEntityDao = printerDbEntityDao;
        mPrefHelper = prefHelper;
    }

    public PrinterDbEntity getActivePrinterDbEntity() {
        long id = mPrefHelper.getActivePrinter();
        return getPrinterFromDbById(id);
    }

    public PrinterDbEntity getPrinterFromDbByName(String name) {
        PrinterDbEntity printerDbEntity;
        try {
            printerDbEntity = mPrinterDbEntityDao.queryBuilder()
                    .where(PrinterDbEntityDao.Properties.Name.eq(name))
                    .unique();
        } catch (Exception e) {
            printerDbEntity = null;
        }

        return printerDbEntity;
    }

    public PrinterDbEntity getPrinterFromDbById(long printerId) {
        PrinterDbEntity printerDbEntity;
        try {
            printerDbEntity = mPrinterDbEntityDao.queryBuilder()
                    .where(PrinterDbEntityDao.Properties.Id.eq(printerId))
                    .unique();
        } catch (Exception e) {
            printerDbEntity = null;
        }

        return printerDbEntity;
    }

    public List<PrinterDbEntity> getPrintersFromDb() {
        return mPrinterDbEntityDao.queryBuilder().list();
    }

    public void deletePrinterInDb(PrinterDbEntity printerDbEntity) {
        PrinterDbEntity oldPrinterDbEntity = getPrinterFromDbByName(printerDbEntity.getName());
        if (oldPrinterDbEntity != null) {
            mPrinterDbEntityDao.delete(oldPrinterDbEntity);
        }
    }

    public long insertOrReplace(PrinterDbEntity printerDbEntity) {
        mPrefHelper.setSaveTimeMillis(System.currentTimeMillis());
        return mPrinterDbEntityDao.insertOrReplace(printerDbEntity);
    }
}
