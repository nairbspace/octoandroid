package com.nairbspace.octoandroid.ui.print_head;

public interface PrintHeadScreen {

    float getJogMultiplier();

    int getFeedRateWithOffset();

    void showToast(String message);

    void setEnableViews(boolean enable);
}
