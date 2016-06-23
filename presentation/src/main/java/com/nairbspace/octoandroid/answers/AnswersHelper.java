package com.nairbspace.octoandroid.answers;

public interface AnswersHelper {
    void addPrinterWithSsl(boolean isSslChecked);
    void loginSuccessWithSsl(boolean isSslChecked);
    void qrClicked();
    void qrWasSuccess();
    void userTriedUnsecureConnection(boolean ok);
}
