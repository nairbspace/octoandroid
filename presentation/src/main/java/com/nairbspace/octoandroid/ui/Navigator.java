package com.nairbspace.octoandroid.ui;

import android.app.Activity;
import android.content.Intent;

import com.nairbspace.octoandroid.ui.add_printer.AddPrinterActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Navigator {
    private final int addPrinterRequestCode = 0;

    @Inject
    public Navigator() {
    }

    public void navigateToAddPrinterActivityForResult(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = AddPrinterActivity.newIntent(activity);
            activity.startActivityForResult(intentToLaunch, addPrinterRequestCode);
        }
    }

    public int getAddPrinterRequestCode() {
        return addPrinterRequestCode;
    }
}
