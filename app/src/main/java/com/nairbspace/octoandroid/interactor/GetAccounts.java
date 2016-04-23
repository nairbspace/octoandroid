package com.nairbspace.octoandroid.interactor;

import android.accounts.Account;

import com.nairbspace.octoandroid.model.OctoAccount;

public interface GetAccounts {

    interface RetrieveListener {
        void onEmpty();
        void onSuccess();
    }

    interface AddAccountListener {

        void onFinishedAddingAccount();
    }

    void retrieveAccounts(RetrieveListener listener);

    void addAccount(OctoAccount octoAccount, AddAccountListener listener);

    void removeAccount(Account account);

    String validateAccountType(String accountType);

    String validateAccountName(String accountName, String ipAddress);
}
