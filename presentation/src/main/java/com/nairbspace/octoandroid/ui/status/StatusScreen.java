package com.nairbspace.octoandroid.ui.status;

public interface StatusScreen {

    void updateUI(String machineState, String octoPrintVersion, String apiVersion);

    void updateMachineState(String machineState);

    void updateFileName(String fileName);

    void updateTime(String time);
}
