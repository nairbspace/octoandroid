package com.nairbspace.octoandroid.ui.status;

import com.nairbspace.octoandroid.data.db.Printer;
import com.nairbspace.octoandroid.interactor.GetPrinterState;
import com.nairbspace.octoandroid.interactor.GetPrinterStateImpl;
import com.nairbspace.octoandroid.net.model.PrinterState;
import com.nairbspace.octoandroid.net.model.Version;
import com.nairbspace.octoandroid.ui.Presenter;

import javax.inject.Inject;

public class StatusPresenter implements Presenter<StatusScreen>, GetPrinterState.GetPrinterStateFinishedListener {

    @Inject GetPrinterStateImpl mGetPrinterState;
    private StatusScreen mScreen;

    @Inject
    public StatusPresenter() {
    }

    @Override
    public void onSuccessDb(Printer printer) {
        if (printer.getVersionJson() != null) {
            String versionJson = printer.getVersionJson();
            Version version = mGetPrinterState.convertJsonToGson(versionJson, Version.class);
            mScreen.updateOctoPrintVersion(version.getServer());
            mScreen.updateApiVersion(version.getApi());
        }
        if (printer.getPrinterStateJson() != null) {
            String printerStateJson = printer.getConnectionJson();
            PrinterState printerState = mGetPrinterState.convertJsonToGson(printerStateJson, PrinterState.class);
            mScreen.updateMachineState(printerState.getState().getText());
        }
    }

    @Override
    public void setScreen(StatusScreen statusScreen) {
        mScreen = statusScreen;
    }

    @Override
    public void onStart() {
        mGetPrinterState.getDataFromDb(this);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        mScreen = null;
    }
}
