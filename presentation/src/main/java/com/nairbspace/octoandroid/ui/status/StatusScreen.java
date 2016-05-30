package com.nairbspace.octoandroid.ui.status;

import android.content.Context;
import android.support.v4.view.PagerAdapter;

public interface StatusScreen {

    void closeDrawer();

    boolean isTabletAndLandscape();

    boolean isDrawerOpen();

    void unlockDrawer();

    void lockDrawer();

    void hideIndicator();

    void syncToggleState();

    void navigateToAddPrinterActivityForResult();

    void displaySnackBar(String message);

    void hideSnackbar();

    void selectStatusNav();

    void setDrawer();

    void setAdapterAndTabLayout(PagerAdapter pagerAdapter);

    void inflateStatusAdapter();

    void updateNavHeader(String printerName, String ipAddress);

    Context context();

    void displaySnackBarAddPrinterFailure();

    void refreshStatusAdapter();
}
