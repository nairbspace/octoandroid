package com.nairbspace.octoandroid.ui.connection;

import com.nairbspace.octoandroid.model.ConnectModel;

import java.util.HashMap;
import java.util.List;

public interface ConnectionScreen {

    void createInitialView();

    void updateUi(ConnectModel connectModel, boolean isUpdateFromPresenter);

    void updateUiWithDefaults(int defaultPortId, int defaultBaudrateId, int defaultPrinterProfileId);

    void updateSerialPortSpinner(List<String> ports);

    void updateBaudRateSpinner(List<Integer> baudrates);

    void updatePrinterProfileSpinner(HashMap<String, String> printerProfiles);

    void showProgressBar(boolean isLoading);

    void showConnectScreen(boolean isNotConnected);

}
