package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.data.db.Printer;
import com.nairbspace.octoandroid.net.model.PrinterState;

public interface GetPrinterState {

    interface GetPrinterStateFinishedListener {
        void onSuccessDb(Printer printer);

        void onPollSuccess(PrinterState printerState);
    }

    void getDataFromDb(GetPrinterStateFinishedListener listener);

    <T> T convertJsonToGson(String json, Class<T> type);
}
