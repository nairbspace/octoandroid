package com.nairbspace.octoandroid.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class AddPrinterActivity extends AppCompatActivity implements AddPrinterScreen,
        TextView.OnEditorActionListener, DialogInterface.OnClickListener,
        View.OnFocusChangeListener, View.OnClickListener,
        QrDialogFragment.OnFragmentInteractionListener {

    public static final int REQUEST_CODE = 0;

    @Inject AddPrinterPresenterImpl mPresenter;

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.add_printer_form) ScrollView mScrollView;
    @Bind(R.id.add_printer_progress) ProgressBar mProgressBar;
    @Bind(R.id.printer_name_edit_text) TextInputEditText mPrinterNameEditText;
    @Bind(R.id.ip_address_edit_text) TextInputEditText mIpAddressEditText;
    @Bind(R.id.port_edit_text) TextInputEditText mPortEditText;
    @Bind(R.id.api_key_edit_text) TextInputEditText mApiKeyEditText;
    @Bind(R.id.ssl_checkbox) CheckBox mSslCheckBox;

    /** Intent used to start activity, extras can be null if started within application **/
    public static Intent newIntent(Context context) {
        return new Intent(context, AddPrinterActivity.class);
    }

    /** Bundle may contain savedInstanceState or bundle from Account Manager */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SetupApplication.get(this).getAppComponent().inject(this);
        setResult(RESULT_CANCELED);

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
        String accountName = mPrinterNameEditText.getText().toString();
        String ipAddress = mIpAddressEditText.getText().toString();
        String port = mPortEditText.getText().toString();
        String apiKey = mApiKeyEditText.getText().toString();
        boolean isSslChecked = mSslCheckBox.isChecked();
        mPresenter.validateCredentials(accountName, ipAddress, port, apiKey, isSslChecked);
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
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.exclamation_triangle)
                .setNegativeButton("Cancel", this)
                .setNeutralButton("Info", this)
                .setPositiveButton("Ok", this)
                .create()
                .show();
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
    public void navigateToPreviousScreen() {
        setResult(RESULT_OK);
        finish();
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
