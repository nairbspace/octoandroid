package com.nairbspace.octoandroid.ui.main;

import android.content.Context;
import android.support.v4.view.PagerAdapter;

public interface MainScreen {

    void closeDrawer();

    boolean isTabletAndLandscape();

    boolean isDrawerOpen();

    void unlockDrawer();

    void lockDrawer();

    void hideIndicator();

    void syncToggleState();

    void navigateToAddPrinterActivityForResult();

    void displaySnackBar(String message);

    void selectStatusNav();

    void setDrawer();

    void setAdapterAndTabLayout(PagerAdapter pagerAdapter);

    void inflateStatusAdapter();

    void updateNavHeader(String printerName, String ipAddress);

    Context context();

    void displaySnackBarAddPrinterFailure();

    void refreshStatusAdapter();
}
