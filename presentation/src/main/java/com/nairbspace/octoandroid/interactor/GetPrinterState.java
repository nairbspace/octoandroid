package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;

public interface GetPrinterState {

    interface GetPrinterStateFinishedListener {
        void onSuccessDb(PrinterDbEntity printerDbEntity);
    }

    void getDataFromDb(GetPrinterStateFinishedListener listener);

    <T> T convertJsonToGson(String json, Class<T> type);
}
