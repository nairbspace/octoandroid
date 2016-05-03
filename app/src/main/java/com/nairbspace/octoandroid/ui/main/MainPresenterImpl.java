package com.nairbspace.octoandroid.ui.main;

import com.nairbspace.octoandroid.data.db.Printer;
import com.nairbspace.octoandroid.interactor.GetAccounts;
import com.nairbspace.octoandroid.interactor.GetAccountsImpl;

import javax.inject.Inject;

public class MainPresenterImpl implements MainPresenter, GetAccounts.RetrieveListener {

    @Inject GetAccountsImpl mGetAccounts;
    private MainScreen mMainScreen;

    @Inject
    public MainPresenterImpl() {
    }

    @Override
    public void setView(MainScreen mainScreen) {
        mMainScreen = mainScreen;
    }

    @Override
    public void getAccounts() {
        mGetAccounts.retrieveAccounts(this);
    }

    @Override
    public void onEmpty() {
        mMainScreen.navigateToAddPrinterActivity();
    }

    @Override
    public void onSuccess(Printer printer) {
        mMainScreen.updateNavHeader(printer.getName(), printer.getHost());
        mMainScreen.displaySnackBar("Success");
    }
}
