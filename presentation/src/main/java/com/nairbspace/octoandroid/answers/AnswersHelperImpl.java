package com.nairbspace.octoandroid.answers;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;

import javax.inject.Inject;

public class AnswersHelperImpl implements AnswersHelper {

    private static final Answers sAnswers = Answers.getInstance();

    @Inject
    public AnswersHelperImpl() {
    }

    @Override
    public void addPrinterWithSsl(boolean isSslChecked) {
        sAnswers.logLogin(new LoginEvent()
                .putMethod("User tried to add printer with SSL")
                .putSuccess(isSslChecked));
    }

    @Override
    public void loginSuccessWithSsl(boolean isSslChecked) {
        sAnswers.logLogin(new LoginEvent()
                .putMethod("Add printer success with SSL")
                .putSuccess(isSslChecked));
    }

    @Override
    public void qrClicked() {
        sAnswers.logLogin(new LoginEvent()
                .putMethod("QR Clicked")
                .putSuccess(true));
    }

    @Override
    public void qrWasSuccess() {
        sAnswers.logLogin(new LoginEvent()
                .putMethod("QR")
                .putSuccess(true));
    }

    @Override
    public void userTriedUnsecureConnection() {
        sAnswers.logLogin(new LoginEvent()
                .putMethod("User tried unsecure connection")
                .putSuccess(true));
    }

    @Override
    public void sslAlertDialog() {
        sAnswers.logLogin(new LoginEvent()
                .putMethod("User received SSL error")
                .putSuccess(true));
    }
}
