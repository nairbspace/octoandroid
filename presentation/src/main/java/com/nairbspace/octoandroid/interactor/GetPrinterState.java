package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.data.db.Printer;

public interface GetPrinterState {

    interface GetPrinterStateFinishedListener {
        void onSuccessDb(Printer printer);
    }

    void getDataFromDb(GetPrinterStateFinishedListener listener);

    <T> T convertJsonToGson(String json, Class<T> type);
}
