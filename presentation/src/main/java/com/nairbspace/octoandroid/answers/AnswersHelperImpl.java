package com.nairbspace.octoandroid.answers;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LevelEndEvent;

import javax.inject.Inject;

public class AnswersHelperImpl implements AnswersHelper {

    private static final Answers sAnswers = Answers.getInstance();

    @Inject
    public AnswersHelperImpl() {
    }

    @Override
    public void addPrinterWithSsl(boolean isSslChecked) {
        sAnswers.logLevelEnd(new LevelEndEvent()
                .putLevelName("User tried to add printer with SSL")
                .putSuccess(isSslChecked));
    }

    @Override
    public void loginSuccessWithSsl(boolean isSslChecked) {
        sAnswers.logLevelEnd(new LevelEndEvent()
                .putLevelName("Add printer success with SSL")
                .putSuccess(isSslChecked));
    }

    @Override
    public void qrClicked() {
        sAnswers.logLevelEnd(new LevelEndEvent()
                .putLevelName("QR Clicked")
                .putSuccess(true));
    }

    @Override
    public void qrWasSuccess() {
        sAnswers.logLevelEnd(new LevelEndEvent()
                .putLevelName("QR")
                .putSuccess(true));
    }

    @Override
    public void userTriedUnsecureConnection(boolean ok) {
        sAnswers.logLevelEnd(new LevelEndEvent()
                .putLevelName("User tried unsecure connection")
                .putSuccess(ok));
    }
}
