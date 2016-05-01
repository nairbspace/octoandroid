package com.nairbspace.octoandroid.ui;

import java.util.List;

public interface ConnectionScreen {
    void updateUI(List<String> ports, List<Integer> baudrates, List<String> printerProfileNames);

    void updateSerialPortSpinner(List<String> ports);

    void updateBaudRateSpinner(List<Integer> baudrates);

    void updatePrinterProfileSpinner(List<String> printerProfileNames);
}
