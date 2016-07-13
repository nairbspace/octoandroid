package com.nairbspace.octoandroid.ui.connection;

import com.nairbspace.octoandroid.model.ConnectModel;

public interface ConnectionScreen {

    void updateUi(ConnectModel connectModel);

    void updateUiWithDefaults(int defaultPortId, int defaultBaudrateId, int defaultPrinterProfileId);

    void showProgressBar(boolean isLoading);

    void setEnableInputViews(boolean shouldEnable);

    void showErrorView();

    void showDisconnectAlert();

}
