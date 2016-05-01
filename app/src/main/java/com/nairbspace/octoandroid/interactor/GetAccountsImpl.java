package com.nairbspace.octoandroid.interactor;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.data.db.Printer;

import javax.inject.Inject;

public class GetAccountsImpl implements GetAccounts {
    @Inject AccountManager mAccountManager;
    @Inject Context mContext;

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
    public void addAccount(Printer printer, AddAccountListener listener) {
        String accountType = "";
        Account account = new Account(printer.getName(), validateAccountType(accountType));

        removeAccount(account); // Cannot overwrite, must delete first

        mAccountManager.addAccountExplicitly(account, null, null);
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
            return mContext.getResources().getString(R.string.account_type);
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
