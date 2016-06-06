package com.nairbspace.octoandroid.ui.temp_controls;

import com.nairbspace.octoandroid.domain.model.TempCommand.ToolBedOffsetTemp;
import com.nairbspace.octoandroid.model.WebsocketModel;

public interface TempControlsScreen {
    void updateUi(WebsocketModel websocketModel);
    void enableButtons(boolean shouldEnable);
    void inputError(ToolBedOffsetTemp toolBedOffsetTemp, String error);
    void toastMessage(String message);
    void hideSoftKeyboard();
}
