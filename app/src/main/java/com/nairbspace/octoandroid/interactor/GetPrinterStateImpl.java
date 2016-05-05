package com.nairbspace.octoandroid.interactor;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.db.Printer;
import com.nairbspace.octoandroid.data.db.PrinterDao;
import com.nairbspace.octoandroid.data.pref.PrefManager;
import com.nairbspace.octoandroid.net.rest.OctoApiImpl;
import com.nairbspace.octoandroid.net.rest.OctoInterceptor;
import com.nairbspace.octoandroid.net.rest.model.PrinterState;

import javax.inject.Inject;

public class GetPrinterStateImpl implements GetPrinterState {

    @Inject PrefManager mPrefManager;
    @Inject PrinterDao mPrinterDao;
    @Inject Gson mGson;
    @Inject OctoInterceptor mInterceptor;
    @Inject OctoApiImpl mApi;
    private Printer mPrinter;

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

    private void savePrinterState(PrinterState printerState) {
        if (mPrinter != null) {
            String printerStateJson = mGson.toJson(printerState);
            mPrinter.setPrinterStateJson(printerStateJson);
            mPrinterDao.update(mPrinter);
        }
    }
}
