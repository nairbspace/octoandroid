package com.nairbspace.octoandroid.ui.connection;

import java.util.List;

public interface ConnectionScreen {
    void updateUI(List<String> ports, List<Integer> baudrates, List<String> printerProfileNames, boolean isNotConnected);

    void updateUiWithDefaults(int defaultPortId, int defaultBaudrateId, int defaultProfileNameId);

    void updateSerialPortSpinner(List<String> ports);

    void updateBaudRateSpinner(List<Integer> baudrates);

    void updatePrinterProfileSpinner(List<String> printerProfileNames);

    void showProgressBar(boolean isLoading);

}
