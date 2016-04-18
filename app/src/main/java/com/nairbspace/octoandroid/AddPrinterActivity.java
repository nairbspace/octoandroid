package com.nairbspace.octoandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPrinterActivity extends AppCompatActivity implements AddPrinterScreen, TextView.OnEditorActionListener {

    private AddPrinterPresenterImpl mPresenter;

    @Bind(R.id.add_printer_form) ScrollView mScrollView;
    @Bind(R.id.add_printer_progress) ProgressBar mProgressBar;
    @Bind(R.id.ip_address_edit_text) TextInputEditText mIpAddressEditText;
    @Bind(R.id.port_edit_text) TextInputEditText mPortEditText;
    @Bind(R.id.api_key_edit_text) TextInputEditText mApiKeyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_printer);
        ButterKnife.bind(this);
        mPresenter = new AddPrinterPresenterImpl(this);
        mApiKeyEditText.setOnEditorActionListener(this);
        mIpAddressEditText.setOnFocusChangeListener(mIpAddressFocusListener);
        mPortEditText.setOnFocusChangeListener(mPortFocusListener);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == R.id.add_printer || actionId == EditorInfo.IME_NULL) {
            onAddPrinterButtonClicked();
            return true;
        }
        return false;
    }

    private View.OnFocusChangeListener mIpAddressFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mIpAddressEditText.setHint("octopi.local");
            } else {
                mIpAddressEditText.setHint("");
            }
        }
    };

    private View.OnFocusChangeListener mPortFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mPortEditText.setHint("443");
            } else {
                mPortEditText.setHint("");
            }
        }
    };

    @OnClick(R.id.qr_button)
    public void onQrButtonClicked() {
        showQrDialogFragment();
    }

    @OnClick(R.id.add_printer_button)
    public void onAddPrinterButtonClicked() {
        String ipAddress = mIpAddressEditText.getText().toString();
        int port = mPresenter.convertPortStringToInt(mPortEditText.getText().toString());
        String apiKey = mApiKeyEditText.getText().toString();
        mPresenter.validateCredentials(ipAddress, port, apiKey);
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
        // Intent start QrDialogFragment
    }

    @Override
    public void showIpAddressError(String error) {
        mIpAddressEditText.setError(error);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
