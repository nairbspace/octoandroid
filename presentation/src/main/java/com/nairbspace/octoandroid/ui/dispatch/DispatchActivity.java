package com.nairbspace.octoandroid.ui.dispatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.templates.BaseActivity;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

public class DispatchActivity extends BaseActivity<DispatchScreen> implements DispatchScreen {

    @Inject DispatchPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(this).getAppComponent().inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getNavigator().wasAddPrinterResultOk(requestCode, resultCode)) {
            navigateToStatusActivity();
        } else {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void navigateToAddPrinterActivityForResult() {
        getNavigator().navigateToAddPrinterActivityForResult(this);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void navigateToStatusActivity() {
        getNavigator().navigateToStatusActivity(this);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected DispatchScreen setScreen() {
        return this;
    }
}
