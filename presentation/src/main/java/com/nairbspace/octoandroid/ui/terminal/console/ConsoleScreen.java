package com.nairbspace.octoandroid.ui.terminal.console;

public interface ConsoleScreen {

    void updateUi(String log);

    void toastMessage(String message);

    void toastError();

    void toastSent();
}
