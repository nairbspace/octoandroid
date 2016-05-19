package com.nairbspace.octoandroid.ui.status;

import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.EventPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class StatusPresenter extends EventPresenter<StatusScreen, WebsocketModel> {

    private StatusScreen mScreen;

    @Inject
    public StatusPresenter(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    protected void onInitialize(StatusScreen statusScreen) {
        mScreen = statusScreen;
    }


    @Override
    protected void onEvent(WebsocketModel websocketModel) {
        mScreen.updateUI(websocketModel);
    }
}
