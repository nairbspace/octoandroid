package com.nairbspace.octoandroid.answers;

import javax.inject.Inject;

/**
 * This class is intentionally blank and gets injected in debug builds because
 * calling methods from {@link com.crashlytics.android.answers.Answers} will crash
 * since instance is never created.
 */
public class AnswersHelperBlank implements AnswersHelper {

    @Inject
    public AnswersHelperBlank() {

    }

    @Override
    public void addPrinterWithSsl(boolean isSslChecked) {

    }

    @Override
    public void loginSuccessWithSsl(boolean isSslChecked) {

    }

    @Override
    public void qrClicked() {

    }

    @Override
    public void qrWasSuccess() {

    }

    @Override
    public void userTriedUnsecureConnection(boolean ok) {

    }

}
