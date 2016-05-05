package com.nairbspace.octoandroid.ui.status;

import com.nairbspace.octoandroid.data.db.Printer;
import com.nairbspace.octoandroid.interactor.GetPrinterState;
import com.nairbspace.octoandroid.interactor.GetPrinterStateImpl;
import com.nairbspace.octoandroid.net.rest.model.PrinterState;
import com.nairbspace.octoandroid.ui.EventPresenter;

import javax.inject.Inject;

public class StatusPresenter extends EventPresenter<StatusScreen, String>
        implements GetPrinterState.GetPrinterStateFinishedListener {

    @Inject GetPrinterStateImpl mGetPrinterState;
    private StatusScreen mScreen;

    @Inject
    public StatusPresenter() {
    }

    @Override
    protected void onInitialize(StatusScreen statusScreen) {
        mScreen = statusScreen;
        mGetPrinterState.getDataFromDb(this);
    }

    @Override
    public void onSuccessDb(Printer printer) {
//        if (printer.getVersionJson() != null) {
//            String versionJson = printer.getVersionJson();
//            Version version = mGetPrinterState.convertJsonToGson(versionJson, Version.class);
//            mScreen.updateOctoPrintVersion(version.getServer());
//            mScreen.updateApiVersion(version.getApi());
//        }
        if (printer.getPrinterStateJson() != null) {
            String printerStateJson = printer.getPrinterStateJson();
            PrinterState printerState = mGetPrinterState
                    .convertJsonToGson(printerStateJson, PrinterState.class);
            if (printerState.getState() != null) {
                mScreen.updateMachineState(printerState.getState().getText());
            }
        }
    }

    @Override
    protected void onEvent(String s) {
        mScreen.updateMachineState(s);
    }
}
