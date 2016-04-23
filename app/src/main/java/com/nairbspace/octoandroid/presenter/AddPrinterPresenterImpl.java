package com.nairbspace.octoandroid.presenter;

import com.nairbspace.octoandroid.interactor.GetPrinter;
import com.nairbspace.octoandroid.interactor.GetPrinterImpl;
import com.nairbspace.octoandroid.interactor.GetAccounts;
import com.nairbspace.octoandroid.interactor.GetAccountsImpl;
import com.nairbspace.octoandroid.model.OctoAccount;
import com.nairbspace.octoandroid.ui.AddPrinterScreen;

import javax.inject.Inject;

public class AddPrinterPresenterImpl implements AddPrinterPresenter,
        GetPrinter.GetPrinterFinishedListener, GetAccounts.AddAccountListener{

    private AddPrinterScreen mAddPrinterScreen;
    @Inject OctoAccount mOctoAccount;
    @Inject
    GetPrinterImpl mAddPrinterInteractor;
    @Inject GetAccountsImpl mGetAccounts;

    @Inject
    public AddPrinterPresenterImpl() {
    }

    @Override
    public void validateCredentials(String accountType, String accountName, String ipAddress,
                                    String port, String apiKey, boolean isSslChecked) {

        if (ipAddress == null || ipAddress.isEmpty()) {
            mAddPrinterScreen.showIpAddressError("IP Address cannot be blank");
            return;
        }

        accountType = mGetAccounts.validateAccountType(accountType);
        ipAddress = mAddPrinterInteractor.extractHost(ipAddress);
        accountName = mGetAccounts.validateAccountName(accountName, ipAddress);
        int portNumber = mAddPrinterInteractor.convertPortStringToInt(port, isSslChecked);
        String scheme = mAddPrinterInteractor.convertIsSslCheckedToScheme(isSslChecked);

        mOctoAccount.setAccount(accountType, accountName, apiKey, scheme, ipAddress, portNumber);

        if (mAddPrinterInteractor.isUrlValid(mOctoAccount)) {
            mAddPrinterInteractor.getVersion(mOctoAccount, this);
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
        mGetAccounts.addAccount(mOctoAccount, this);
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
        mAddPrinterScreen.navigateToPreviousScreen(mOctoAccount);
    }
}
