package com.nairbspace.octoandroid.ui.add_printer;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.interactor.GetAccounts;
import com.nairbspace.octoandroid.interactor.GetAccountsImpl;
import com.nairbspace.octoandroid.interactor.GetPrinter;
import com.nairbspace.octoandroid.interactor.GetPrinterImpl;
import com.nairbspace.octoandroid.ui.Presenter;

import javax.inject.Inject;

public class AddPrinterPresenter extends Presenter<AddPrinterScreen> implements
        GetPrinter.GetPrinterFinishedListener, GetAccounts.AddAccountListener{

    private AddPrinterScreen mScreen;
    private PrinterDbEntity mPrinterDbEntity;
    @Inject GetPrinterImpl mAddPrinterInteractor;
    @Inject GetAccountsImpl mGetAccounts;

    @Inject
    public AddPrinterPresenter() {
        mPrinterDbEntity = new PrinterDbEntity();
    }

    public void validateCredentials(String accountName, String ipAddress,
                                    String port, String apiKey, boolean isSslChecked) {

        if (ipAddress == null || ipAddress.isEmpty()) {
            mScreen.showIpAddressError("IP Address cannot be blank");
            return;
        }

        ipAddress = mAddPrinterInteractor.extractHost(ipAddress);
        accountName = mGetAccounts.validateAccountName(accountName, ipAddress);
        int portNumber = mAddPrinterInteractor.convertPortStringToInt(port, isSslChecked);
        String scheme = mAddPrinterInteractor.convertIsSslCheckedToScheme(isSslChecked);

        mPrinterDbEntity = mAddPrinterInteractor.setPrinter(mPrinterDbEntity, accountName,
                apiKey, scheme, ipAddress, portNumber);

        if (mAddPrinterInteractor.isUrlValid(mPrinterDbEntity)) {
            mAddPrinterInteractor.getVersion(mPrinterDbEntity, this);
        } else {
            mScreen.showIpAddressError("Incorrect formatting");
        }
    }

    @Override
    protected void onInitialize(AddPrinterScreen addPrinterScreen) {
        mScreen = addPrinterScreen;
    }

    @Override
    public void onLoading() {
        mScreen.hideSoftKeyboard(true);
        mScreen.showProgress(true);
    }

    @Override
    public void onComplete() {
        mScreen.showProgress(false);
    }

    @Override
    public void onSuccess() {
        mScreen.showSnackbar("Success");
        mGetAccounts.addAccount(mPrinterDbEntity, this);

    }

    @Override
    public void onFailure() {
        mScreen.showSnackbar("Failure");
    }

    @Override
    public void onSslFailure() {
        mScreen.showAlertDialog("SSL Error",
                "SSL Certificate is not signed. If accessing printerDetails locally try unsecure connection.");
    }

    @Override
    public void onApiKeyFailure() {
        mScreen.showSnackbar("Invalid API key");
    }

    @Override
    public void onFinishedAddingAccount() {
        mScreen.navigateToPreviousScreen();
    }
}
