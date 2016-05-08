package com.nairbspace.octoandroid.interactor;

import android.accounts.Account;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;

public interface GetAccounts {

    interface RetrieveListener {
        void onEmpty();
        void onSuccess(PrinterDbEntity printerDbEntity);
    }

    interface AddAccountListener {

        void onFinishedAddingAccount();
    }

    void retrieveAccounts(RetrieveListener listener);

    void addAccount(PrinterDbEntity printerDbEntity, AddAccountListener listener);

    void removeAccount(Account account);

    String validateAccountType(String accountType);

    String validateAccountName(String accountName, String ipAddress);
}
