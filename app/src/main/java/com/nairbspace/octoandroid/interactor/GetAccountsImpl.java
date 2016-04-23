package com.nairbspace.octoandroid.interactor;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.nairbspace.octoandroid.model.OctoAccount;

import javax.inject.Inject;

public class GetAccountsImpl implements GetAccounts {
    @Inject AccountManager mAccountManager;

    @Inject
    public GetAccountsImpl() {
    }


    @Override
    public void retrieveAccounts(RetrieveListener listener) {
        Account[] accounts = mAccountManager.getAccounts();
        if (accounts.length == 0) {
            listener.onEmpty();
        } else {
            listener.onSuccess();
        }
    }

    @Override
    public void addAccount(OctoAccount octoAccount, GetAccounts.AddAccountListener listener) {

        Account account = new Account(octoAccount.getAccountName(), octoAccount.getAccountType());

        Bundle userdata = new Bundle();
        userdata.putString(OctoAccount.API_KEY_KEY, octoAccount.getApiKey());
        userdata.putString(OctoAccount.SCHEME_KEY, octoAccount.getScheme());
        userdata.putString(OctoAccount.HOST_KEY, octoAccount.getHost());
        userdata.putInt(OctoAccount.PORT_KEY, octoAccount.getPort());

        removeAccount(account); // Cannot overwrite, must delete first

        mAccountManager.addAccountExplicitly(account, null, userdata);
        listener.onFinishedAddingAccount();
    }

    @Override
    public void removeAccount(Account account) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mAccountManager.removeAccountExplicitly(account);
        } else {
            mAccountManager.removeAccount(account, null, null);
        }
    }

    @Override
    public String validateAccountType(String accountType) {
        if (TextUtils.isEmpty(accountType)) {
            return OctoAccount.DEFAULT_ACCOUNT_TYPE;
        }
        return accountType;
    }

    @Override
    public String validateAccountName(String accountName, String ipAddress) {
        if (TextUtils.isEmpty(accountName)) {
            return ipAddress;
        }
        return accountName;
    }
}
