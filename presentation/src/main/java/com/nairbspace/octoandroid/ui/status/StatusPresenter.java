package com.nairbspace.octoandroid.ui.status;

import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

public class StatusPresenter extends Presenter<StatusScreen> {

    private StatusScreen mScreen;

    @Inject
    public StatusPresenter() {
    }

    @Override
    protected void onInitialize(StatusScreen statusScreen) {
        mScreen = statusScreen;
    }
}
