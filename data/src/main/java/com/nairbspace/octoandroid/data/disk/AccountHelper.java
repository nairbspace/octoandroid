package com.nairbspace.octoandroid.data.disk;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Build;
import android.text.TextUtils;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;

import javax.inject.Inject;

/** Convenience methods for AccountManager related info */
public class AccountHelper {

    private final AccountManager mAccountManager;

    @Inject
    public AccountHelper(AccountManager accountManager) {
        mAccountManager = accountManager;
    }

    public boolean doesPrinterExistInAccountManager(PrinterDbEntity printerDbEntity) {
        Account account = findPrinterAccount(printerDbEntity);
        return account != null;
    }

    public Account findPrinterAccount(PrinterDbEntity printerDbEntity) {
        Account[] accounts = mAccountManager.getAccounts();
        if (accounts.length == 0) {
            return null;
        }

        for (Account account : accounts) {
            if (account.name.equals(printerDbEntity.getName())) {
                return account;
            }
        }
        return null;
    }

    private String validateAccountType(String accountType) {
        if (TextUtils.isEmpty(accountType)) {
            return "com.nairbspace.actoandroid"; // TODO should add in data res folder
        }
        return accountType;
    }

    public void removeAccount(PrinterDbEntity printerDbEntity) {
        Account account = findPrinterAccount(printerDbEntity);
        if (account != null) {
            removeAccount(account);
        }
    }

    private void removeAccount(Account account) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mAccountManager.removeAccountExplicitly(account);
        } else {
            //noinspection deprecation
            mAccountManager.removeAccount(account, null, null);
        }
    }

    public void addAccount(PrinterDbEntity printerDbEntity) {
        String accountType = "";
        Account account = new Account(printerDbEntity.getName(), validateAccountType(accountType));

        removeAccount(account); // Cannot overwrite, must delete first

        mAccountManager.addAccountExplicitly(account, null, null);
    }
}
