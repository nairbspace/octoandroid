package com.nairbspace.octoandroid.ui.status;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.PrinterStateEntity;
import com.nairbspace.octoandroid.interactor.GetPrinterState;
import com.nairbspace.octoandroid.interactor.GetPrinterStateImpl;
import com.nairbspace.octoandroid.ui.EventPresenter;

import javax.inject.Inject;

public class StatusPresenter extends EventPresenter<StatusScreen, String[]>
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
    public void onSuccessDb(PrinterDbEntity printerDbEntity) {
//        if (printerDetails.getVersionJson() != null) {
//            String versionJson = printerDetails.getVersionJson();
//            Version version = mGetPrinterState.convertJsonToGson(versionJson, Version.class);
//            mScreen.updateFileName(version.getServer());
//            mScreen.updateTime(version.getApi());
//        }
        if (printerDbEntity.getPrinterStateJson() != null) {
            String printerStateJson = printerDbEntity.getPrinterStateJson();
            PrinterStateEntity printerStateEntity = mGetPrinterState
                    .convertJsonToGson(printerStateJson, PrinterStateEntity.class);
            if (printerStateEntity.state() != null) {
                mScreen.updateMachineState(printerStateEntity.state().text());
            }
        }
    }

    @Override
    protected void onEvent(String[] strings) {
        mScreen.updateMachineState(strings[0]);
        mScreen.updateFileName(strings[1]);
        mScreen.updateTime(strings[2]);
    }
}
