package com.nairbspace.octoandroid.ui;

public interface MainScreen {

    void closeDrawer();

    boolean isTabletAndLandscape();

    boolean isDrawerOpen();

    void unlockDrawer();

    void lockDrawer();

    void hideIndicator();

    void syncToggleState();

    void navigateToAddPrinterActivity();

    void displaySnackBar(String message);
}
