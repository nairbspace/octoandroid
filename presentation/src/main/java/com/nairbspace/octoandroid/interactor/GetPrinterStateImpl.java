package com.nairbspace.octoandroid.interactor;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.db.PrinterDbEntityDao;
import com.nairbspace.octoandroid.data.disk.PrefHelper;
import com.nairbspace.octoandroid.data.entity.PrinterStateEntity;
import com.nairbspace.octoandroid.data.net.OctoInterceptor;

import javax.inject.Inject;

public class GetPrinterStateImpl implements GetPrinterState {

    @Inject
    PrefHelper mPrefHelper;
    @Inject
    PrinterDbEntityDao mPrinterDbEntityDao;
    @Inject Gson mGson;
    @Inject OctoInterceptor mInterceptor;
    private PrinterDbEntity mPrinterDbEntity;

    @Inject
    public GetPrinterStateImpl() {
    }

    @Override
    public void getDataFromDb(GetPrinterStateFinishedListener listener) {
        long id = mPrefHelper.getActivePrinter();
        if (id != PrefHelper.NO_ACTIVE_PRINTER) {
            mPrinterDbEntity = mPrinterDbEntityDao.queryBuilder()
                    .where(PrinterDbEntityDao.Properties.Id.eq(id))
                    .unique();

            if (mPrinterDbEntity != null) {
                listener.onSuccessDb(mPrinterDbEntity);
            }
        }
    }

    @Override
    public <T> T convertJsonToGson(String json, Class<T> type) {
        return mGson.fromJson(json, type);
    }

    private void savePrinterState(PrinterStateEntity printerStateEntity) {
        if (mPrinterDbEntity != null) {
            String printerStateJson = mGson.toJson(printerStateEntity);
            mPrinterDbEntity.setPrinterStateJson(printerStateJson);
            mPrinterDbEntityDao.update(mPrinterDbEntity);
        }
    }
}
