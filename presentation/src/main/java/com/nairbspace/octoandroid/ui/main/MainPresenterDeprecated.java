package com.nairbspace.octoandroid.ui.main;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.interactor.GetAccounts;
import com.nairbspace.octoandroid.interactor.GetAccountsImpl;
import com.nairbspace.octoandroid.ui.Presenter;

import javax.inject.Inject;

public class MainPresenterDeprecated extends Presenter<MainScreen> implements GetAccounts.RetrieveListener {

    @Inject GetAccountsImpl mGetAccounts;
    private MainScreen mScreen;

    @Inject
    public MainPresenterDeprecated() {
    }

    @Override
    public void onEmpty() {
        mScreen.navigateToAddPrinterActivityForResult();
    }

    @Override
    public void onSuccess(PrinterDbEntity printerDbEntity) {
        mScreen.updateNavHeader(printerDbEntity.getName(), printerDbEntity.getHost());
        mScreen.displaySnackBar("Success");
    }

    @Override
    protected void onInitialize(MainScreen mainScreen) {
        mScreen = mainScreen;
        getAccounts();
    }

    protected void getAccounts() {
        mGetAccounts.retrieveAccounts(this);
    }
}
