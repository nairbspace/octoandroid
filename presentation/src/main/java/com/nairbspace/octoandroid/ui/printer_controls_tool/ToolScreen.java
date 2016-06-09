package com.nairbspace.octoandroid.ui.printer_controls_tool;

public interface ToolScreen {

    int getFlowrateWithOffset();

    String getAmount();

    int getTool();

    void showInputAmountError(String message);

    void showToast(String message);
}
