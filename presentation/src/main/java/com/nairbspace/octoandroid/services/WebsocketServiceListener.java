package com.nairbspace.octoandroid.services;

import com.nairbspace.octoandroid.model.WebsocketModel;

public interface WebsocketServiceListener {
    void turnOffAlarmAndStopService();
    void showSticky(WebsocketModel model);
    void showFinishedAndDestroy(WebsocketService.FinishType type, String fileName);
    void checkApplicationStatus();
}
