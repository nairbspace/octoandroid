package com.nairbspace.octoandroid.presenter;

import com.nairbspace.octoandroid.interactor.GetPrinter;
import com.nairbspace.octoandroid.interactor.GetPrinterImpl;
import com.nairbspace.octoandroid.interactor.GetAccounts;
import com.nairbspace.octoandroid.interactor.GetAccountsImpl;
import com.nairbspace.octoandroid.model.Printer;
import com.nairbspace.octoandroid.ui.add_printer.AddPrinterScreen;

import javax.inject.Inject;

public class AddPrinterPresenterImpl implements AddPrinterPresenter,
        GetPrinter.GetPrinterFinishedListener, GetAccounts.AddAccountListener{

    private AddPrinterScreen mAddPrinterScreen;
    private Printer mPrinter;
    @Inject GetPrinterImpl mAddPrinterInteractor;
    @Inject GetAccountsImpl mGetAccounts;

    @Inject
    public AddPrinterPresenterImpl() {
        mPrinter = new Printer();
    }

    @Override
    public void validateCredentials(String accountName, String ipAddress,
                                    String port, String apiKey, boolean isSslChecked) {

        if (ipAddress == null || ipAddress.isEmpty()) {
            mAddPrinterScreen.showIpAddressError("IP Address cannot be blank");
            return;
        }

        ipAddress = mAddPrinterInteractor.extractHost(ipAddress);
        accountName = mGetAccounts.validateAccountName(accountName, ipAddress);
        int portNumber = mAddPrinterInteractor.convertPortStringToInt(port, isSslChecked);
        String scheme = mAddPrinterInteractor.convertIsSslCheckedToScheme(isSslChecked);

        mPrinter = mAddPrinterInteractor.setPrinter(mPrinter, accountName,
                apiKey, scheme, ipAddress, portNumber);

        if (mAddPrinterInteractor.isUrlValid(mPrinter)) {
            mAddPrinterInteractor.getVersion(mPrinter, this);
        } else {
            mAddPrinterScreen.showIpAddressError("Incorrect formatting");
        }
    }

    @Override
    public void onDestroy() {
        mAddPrinterScreen = null;
    }

    @Override
    public void setView(AddPrinterScreen addPrinterScreen) {
        mAddPrinterScreen = addPrinterScreen;
    }

    @Override
    public void onLoading() {
        mAddPrinterScreen.hideSoftKeyboard(true);
        mAddPrinterScreen.showProgress(true);
    }

    @Override
    public void onComplete() {
        mAddPrinterScreen.showProgress(false);
    }

    @Override
    public void onSuccess() {
        mAddPrinterScreen.showSnackbar("Success");
        mGetAccounts.addAccount(mPrinter, this);

    }

    @Override
    public void onFailure() {
        mAddPrinterScreen.showSnackbar("Failure");
    }

    @Override
    public void onSslFailure() {
        mAddPrinterScreen.showAlertDialog("SSL Error",
                "SSL Certificate is not signed. If accessing printer locally try unsecure connection.");
    }

    @Override
    public void onApiKeyFailure() {
        mAddPrinterScreen.showSnackbar("Invalid API key");
    }

    @Override
    public void onFinishedAddingAccount() {
        mAddPrinterScreen.navigateToPreviousScreen();
    }
}
