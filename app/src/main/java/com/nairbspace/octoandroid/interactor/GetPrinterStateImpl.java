package com.nairbspace.octoandroid.interactor;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.db.Printer;
import com.nairbspace.octoandroid.data.db.PrinterDao;
import com.nairbspace.octoandroid.data.pref.PrefManager;

import javax.inject.Inject;

public class GetPrinterStateImpl implements GetPrinterState {

    @Inject
    PrefManager mPrefManager;
    @Inject
    PrinterDao mPrinterDao;
    @Inject
    Gson mGson;

    @Inject
    public GetPrinterStateImpl() {
    }

    @Override
    public void getDataFromDb(GetPrinterStateFinishedListener listener) {
        long id = mPrefManager.getActivePrinter();
        if (id != PrefManager.NO_ACTIVE_PRINTER) {
            Printer printer = mPrinterDao.queryBuilder()
                    .where(PrinterDao.Properties.Id.eq(id))
                    .unique();

            if (printer != null) {
                listener.onSuccessDb(printer);
            }
        }
    }

    @Override
    public <T> T convertJsonToGson(String json, Class<T> type) {
        return mGson.fromJson(json, type);
    }
}
