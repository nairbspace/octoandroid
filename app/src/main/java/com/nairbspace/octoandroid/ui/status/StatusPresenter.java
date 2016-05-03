package com.nairbspace.octoandroid.ui.status;

import com.nairbspace.octoandroid.data.db.Printer;
import com.nairbspace.octoandroid.interactor.GetPrinterState;
import com.nairbspace.octoandroid.interactor.GetPrinterStateImpl;
import com.nairbspace.octoandroid.net.model.PrinterState;
import com.nairbspace.octoandroid.net.model.Version;
import com.nairbspace.octoandroid.ui.Presenter;

import javax.inject.Inject;

public class StatusPresenter extends Presenter<StatusScreen>
        implements GetPrinterState.GetPrinterStateFinishedListener {

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
            String printerStateJson = printer.getPrinterStateJson();
            PrinterState printerState = mGetPrinterState
                    .convertJsonToGson(printerStateJson, PrinterState.class);
            if (printerState.getState() != null) {
                mScreen.updateMachineState(printerState.getState().getText());
            }
        }
    }

    @Override
    public void onPollSuccess(PrinterState printerState) {
        try {
            PrinterState.State state = printerState.getState();
            String machineState = state.getText();
            mScreen.updateMachineState(machineState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onInitialize(StatusScreen statusScreen) {
        mScreen = statusScreen;
        mGetPrinterState.getDataFromDb(this);
    }

    @Override
    protected void isVisibleToUser() {
        mGetPrinterState.pollPrinterState(this);
    }

    @Override
    protected void isNotVisibleToUser() {
        mGetPrinterState.unsubscribePollSubscription();
    }
}
