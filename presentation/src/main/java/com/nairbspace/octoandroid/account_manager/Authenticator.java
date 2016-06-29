package com.nairbspace.octoandroid.account_manager;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.SyncDbAndAccountDeletion;
import com.nairbspace.octoandroid.ui.add_printer.AddPrinterActivity;

import javax.inject.Inject;

public class Authenticator extends AbstractAccountAuthenticator {

    private final Context mContext;
//    private final Handler mHandler;
    @Inject SyncDbAndAccountDeletion mSyncWithDb;

    public Authenticator(Context context) {
        super(context);
        mContext = context;
//        mHandler = new Handler();
        SetupApplication.get(context).getAppComponent().inject(this);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Intent i = AddPrinterActivity.newIntent(mContext);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, i);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response, Account account) throws NetworkErrorException {
        Bundle result = new Bundle();

        // Code below works, but now can't delete from within app...
//        // Check if app process is running before deleting account
//        if (LifecycleHandler.isApplicationInBackground()) {
//            result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
//            mHandler.post(toastRunnable);
//        } else {
//            String name = account.name;
//            mPrinterDetailsByName.execute(new GetPrinterDetailsSubscriber(), name);
//            result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
//        }

        String name = account.name;
        mSyncWithDb.execute(new DefaultSubscriber() {}, name);
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
        return result;
    }

//    private Runnable toastRunnable = new Runnable() {
//        @Override
//        public void run() {
//            String message = mContext.getString(R.string.app_running_cannot_delete_account);
//            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
//        }
//    };
}
