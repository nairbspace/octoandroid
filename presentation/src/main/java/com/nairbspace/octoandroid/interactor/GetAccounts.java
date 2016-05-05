package com.nairbspace.octoandroid.interactor;

import android.accounts.Account;

import com.nairbspace.octoandroid.data.db.Printer;

public interface GetAccounts {

    interface RetrieveListener {
        void onEmpty();
        void onSuccess(Printer printer);
    }

    interface AddAccountListener {

        void onFinishedAddingAccount();
    }

    void retrieveAccounts(RetrieveListener listener);

    void addAccount(Printer printer, AddAccountListener listener);

    void removeAccount(Account account);

    String validateAccountType(String accountType);

    String validateAccountName(String accountName, String ipAddress);
}
