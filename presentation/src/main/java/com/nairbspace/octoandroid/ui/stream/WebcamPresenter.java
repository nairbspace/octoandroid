package com.nairbspace.octoandroid.ui.stream;

import com.nairbspace.octoandroid.ui.Presenter;

import javax.inject.Inject;

public class WebcamPresenter extends Presenter<WebcamScreen> {

    private WebcamScreen mScreen;

    @Inject
    public WebcamPresenter() {
    }

    @Override
    protected void onInitialize(WebcamScreen webcamScreen) {
        mScreen = webcamScreen;
    }
}
