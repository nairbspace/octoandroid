package com.nairbspace.octoandroid.presenter;

import com.nairbspace.octoandroid.interactor.GetAccounts;
import com.nairbspace.octoandroid.interactor.GetAccountsImpl;
import com.nairbspace.octoandroid.ui.MainScreen;

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
    public void onSuccess() {
        mMainScreen.selectStatusNav();
        mMainScreen.displaySnackBar("Success");
    }
}
