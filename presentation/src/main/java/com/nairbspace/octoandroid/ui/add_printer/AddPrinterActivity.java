package com.nairbspace.octoandroid.ui.add_printer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.answers.AnswersHelper;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.AddPrinterModel;
import com.nairbspace.octoandroid.ui.templates.BaseActivity;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPrinterActivity extends BaseActivity<AddPrinterScreen>
        implements AddPrinterScreen, QrDialogFragment.Listener, SslErrorDialogFragment.Listener {
    private static final String ADD_PRINTER_MODEL_KEY = "add_printer_model_key";
    @BindString(R.string.exception_no_rear_camera) String NO_REAR_CAMERA;

    @Inject AddPrinterPresenter mPresenter;
    @Inject AnswersHelper mAnswersHelper;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.add_printer_form) ScrollView mScrollView;
    @BindView(R.id.add_printer_progress) ProgressBar mProgressBar;
    @BindView(R.id.printer_name_edit_text) TextInputEditText mPrinterNameEditText;
    @BindView(R.id.ip_address_edit_text) TextInputEditText mIpAddressEditText;
    @BindView(R.id.port_edit_text) TextInputEditText mPortEditText;
    @BindView(R.id.api_key_edit_text) TextInputEditText mApiKeyEditText;
    @BindView(R.id.ssl_checkbox) CheckBox mSslCheckBox;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddPrinterActivity.class);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SetupApplication.get(this).getAppComponent().inject(this);
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_add_printer);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mApiKeyEditText.setOnEditorActionListener(mApiEditorListener);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState != null &&
                savedInstanceState.getParcelable(ADD_PRINTER_MODEL_KEY) != null) {
            AddPrinterModel model = savedInstanceState.getParcelable(ADD_PRINTER_MODEL_KEY);
            updateUi(model);
        }
    }

    private void updateUi(AddPrinterModel addPrinterModel) {
        mPrinterNameEditText.setText(addPrinterModel.accountName());
        mIpAddressEditText.setText(addPrinterModel.ipAddress());
        mPortEditText.setText(addPrinterModel.port());
        mApiKeyEditText.setText(addPrinterModel.apiKey());
        mSslCheckBox.setChecked(addPrinterModel.isSslChecked());
    }

    private final TextView.OnEditorActionListener mApiEditorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onAddPrinterButtonClicked();
            }
            return false;
        }
    };

    @OnClick(R.id.qr_button) void onQrButtonClicked() {
        showQrDialogFragment();
        mAnswersHelper.qrClicked();
    }

    @OnClick(R.id.add_printer_button) void onAddPrinterButtonClicked() {
        AddPrinterModel model = getAddPrinterModel();
        mPresenter.onAddPrinterClicked(model);
        mAnswersHelper.addPrinterWithSsl(model.isSslChecked());
    }

    private AddPrinterModel getAddPrinterModel() {
        return AddPrinterModel.builder()
                .accountName(mPrinterNameEditText.getText().toString())
                .ipAddress(mIpAddressEditText.getText().toString())
                .port(mPortEditText.getText().toString())
                .apiKey(mApiKeyEditText.getText().toString())
                .isSslChecked(mSslCheckBox.isChecked())
                .build();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ADD_PRINTER_MODEL_KEY, getAddPrinterModel());
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
        if (PlayServiceChecker.isAvailable(this) && doesRearCameraExist()) {
            DialogFragment df = QrDialogFragment.newInstance();
            df.setCancelable(true);
            df.show(getSupportFragmentManager(), null);
        }
    }

    private boolean doesRearCameraExist() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            showSnackbar(NO_REAR_CAMERA);
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PlayServiceChecker.REQUEST_CODE && resultCode == RESULT_OK) {
            showQrDialogFragment();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showIpAddressError(String error) {
        mIpAddressEditText.setError(error);
    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(mScrollView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, mSnackBarOnClickListener).show();
    }

    private final View.OnClickListener mSnackBarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Do nothing in particular
        }
    };

    @Override
    public void showAlertDialog() {
        SslErrorDialogFragment sslDialog = SslErrorDialogFragment.newInstance();
        sslDialog.setCancelable(true);
        sslDialog.show(getSupportFragmentManager(), null);
        mAnswersHelper.sslAlertDialog();
    }

    @Override
    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void navigateToPreviousScreen() {
        mAnswersHelper.loginSuccessWithSsl(getAddPrinterModel().isSslChecked());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public Context context() {
        return this;
    }


    @Override
    public void onQrSuccess(String apiKey) {
        mApiKeyEditText.setText(apiKey);
        mAnswersHelper.qrWasSuccess();
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected AddPrinterScreen setScreen() {
        return this;
    }

    @Override
    public void tryUnsecureConnection() {
        mSslCheckBox.setChecked(false);
        onAddPrinterButtonClicked();
        mAnswersHelper.userTriedUnsecureConnection();
    }
}
