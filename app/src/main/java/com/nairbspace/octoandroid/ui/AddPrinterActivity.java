package com.nairbspace.octoandroid.ui;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.presenter.AddPrinterPresenterImpl;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class AddPrinterActivity extends AuthenticatorActivity implements AddPrinterScreen, TextView.OnEditorActionListener,
        DialogInterface.OnClickListener, View.OnFocusChangeListener, View.OnClickListener, QrDialogFragment.OnFragmentInteractionListener {

    @Inject AddPrinterPresenterImpl mPresenter;
    @Inject AccountManager mAccountManager;

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.add_printer_form) ScrollView mScrollView;
    @Bind(R.id.add_printer_progress) ProgressBar mProgressBar;
    @Bind(R.id.printer_name_edit_text) TextInputEditText mPrinterNameEditText;
    @Bind(R.id.ip_address_edit_text) TextInputEditText mIpAddressEditText;
    @Bind(R.id.port_edit_text) TextInputEditText mPortEditText;
    @Bind(R.id.api_key_edit_text) TextInputEditText mApiKeyEditText;
    @Bind(R.id.ssl_checkbox) CheckBox mSslCheckBox;


    /** Intent used to start activity, extras can be null if started within application **/
    public static Intent newIntent(Context context, AccountAuthenticatorResponse response, String accountType) {
        Intent i = new Intent(context, AddPrinterActivity.class);
        i.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        i.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        return i;
    }

    /** Bundle may contain savedInstanceState or bundle from Account Manager */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SetupApplication.get(this).getAppComponent().inject(this);

        setContentView(R.layout.activity_add_printer);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mPresenter.setView(this);
        mApiKeyEditText.setOnEditorActionListener(this);
        mIpAddressEditText.setOnFocusChangeListener(this);
        mPortEditText.setOnFocusChangeListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == R.id.add_printer || actionId == EditorInfo.IME_NULL) {
            onAddPrinterButtonClicked();
            return true;
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == mIpAddressEditText) {
            if (hasFocus) {
                mIpAddressEditText.setHint("octopi.local");
            } else {
                mIpAddressEditText.setHint("");
            }
        }

        if (v == mPortEditText) {
            if (hasFocus) {
                if (mSslCheckBox.isChecked()) {
                    mPortEditText.setHint("443");
                } else {
                    mPortEditText.setHint("80");
                }
            } else {
                mPortEditText.setHint("");
            }
        }
    }

    @OnClick(R.id.qr_button)
    public void onQrButtonClicked() {
        showQrDialogFragment();
    }

    @OnClick(R.id.add_printer_button)
    public void onAddPrinterButtonClicked() {
        String ipAddress = mIpAddressEditText.getText().toString();
        boolean isSslChecked = mSslCheckBox.isChecked();
        int port = mPresenter.convertPortStringToInt(mPortEditText.getText().toString(), isSslChecked);
        String apiKey = mApiKeyEditText.getText().toString();
        mPresenter.validateCredentials(isSslChecked, ipAddress, port, apiKey);
    }

    @Override
    public void showProgress(final Boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        mScrollView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void showQrDialogFragment() {
        DialogFragment df = QrDialogFragment.newInstance();
        df.setCancelable(true);
        df.show(getSupportFragmentManager(), null);
    }

    @Override
    public void showIpAddressError(String error) {
        mIpAddressEditText.setError(error);
    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(mScrollView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Ok", this).show();
    }

    @Override
    public void showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.exclamation_triangle)
                .setNegativeButton("Cancel", this)
                .setNeutralButton("Info", this)
                .setPositiveButton("Ok", this)
                .create();
        alertDialog.show();
    }

    @Override
    public void hideSoftKeyboard(boolean show) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (show) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        } else {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }

    @Override
    public void addAccount(String scheme, String host, int port, String apiKey) {
        String accountName = mPrinterNameEditText.getText().toString();
        if (TextUtils.isEmpty(accountName)) {
            accountName = mIpAddressEditText.getText().toString();
        }

        String accountType = getIntent().getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
        if (TextUtils.isEmpty(accountType)) {
            accountType = getResources().getString(R.string.account_type);
        }

        Account account = new Account(accountName, accountType);

        Bundle userdata = new Bundle();
        Resources res = getResources();
        userdata.putString(res.getString(R.string.api_key_key), apiKey);
        userdata.putString(res.getString(R.string.scheme_key), scheme);
        userdata.putString(res.getString(R.string.host_key), host);
        userdata.putInt(res.getString(R.string.port_key), port);

        AccountAuthenticatorResponse response = getIntent()
                .getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        /**
         * Delete the account if it already exists, if trying to add account that already exists
         * it will not work.
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mAccountManager.removeAccountExplicitly(account);
        } else {
            mAccountManager.removeAccount(account, null, null);
        }

        mAccountManager.addAccountExplicitly(account, null, userdata);

        /** If Activity is started from Account Manager then feed it back the information
         * it needs and finish Activity */
        if (response != null) {

            /** Adding all the intent extras currently does nothing since account has already
             * been added above, but might be useful later on.*/
            Intent addAccount = getIntent();
            addAccount.putExtra(AccountManager.KEY_ACCOUNT_NAME, accountName);
            addAccount.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
            addAccount.putExtra(AccountManager.KEY_USERDATA, userdata);
            setAccountAuthenticatorResult(addAccount.getExtras());
            setResult(RESULT_OK, addAccount);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                break;
            case DialogInterface.BUTTON_POSITIVE:
                mSslCheckBox.setChecked(false);
                onAddPrinterButtonClicked();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.support.design.R.id.snackbar_action:
                Timber.d("This is a snackbar");
                break;
        }
    }

    @Override
    public void onQrSuccess(String apiKey) {
        mApiKeyEditText.setText(apiKey);
    }
}
