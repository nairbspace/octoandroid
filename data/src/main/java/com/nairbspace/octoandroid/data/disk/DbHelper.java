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
        long id = mPrefHelper.getActivePrinterId();
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
        try {
            return mPrinterDbEntityDao.queryBuilder().list();
        } catch (Exception e) {
            return null;
        }
    }

    public void deletePrinterInDb(PrinterDbEntity printerDbEntity) {
        PrinterDbEntity oldPrinterDbEntity;
        if (printerDbEntity.getId() != null) {
            oldPrinterDbEntity = getPrinterFromDbById(printerDbEntity.getId());
        } else {
            oldPrinterDbEntity = getPrinterFromDbByName(printerDbEntity.getName());
        }
        if (oldPrinterDbEntity != null) {
            mPrinterDbEntityDao.delete(oldPrinterDbEntity);
        }
    }

    public long insertOrReplace(PrinterDbEntity printerDbEntity) {
        mPrefHelper.setSaveTimeMillis(System.currentTimeMillis());
        return mPrinterDbEntityDao.insertOrReplace(printerDbEntity);
    }

    /**
     *
     * @param entity the entity to be checked
     * @return the result if entity name already exists as with a different id
     */
    public boolean doesPrinterNameExist(PrinterDbEntity entity) {
        List<PrinterDbEntity> list = getPrintersFromDb();
        if (list == null) return false;
        for (PrinterDbEntity printerDbEntity : list) {
            String dbName = printerDbEntity.getName();
            String editName = entity.getName();
            long dbId = printerDbEntity.getId();
            long editId = entity.getId();
            if (dbName.equals(editName) && dbId != editId) {
                return true;
            }
        }
        return false;
    }
}
