package com.nairbspace.octoandroid.ui.print_head;

import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

public class PrintHeadPresenter extends Presenter<PrintHeadScreen> {

    private PrintHeadScreen mScreen;

    @Inject
    public PrintHeadPresenter() {
    }

    @Override
    protected void onInitialize(PrintHeadScreen printHeadScreen) {
        mScreen = printHeadScreen;
    }
}
