package com.nairbspace.octoandroid.ui.state;

import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.EventPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class StatePresenter extends EventPresenter<StateScreen, WebsocketModel> {

    private StateScreen mScreen;

    @Inject
    public StatePresenter(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    protected void onInitialize(StateScreen stateScreen) {
        mScreen = stateScreen;
    }


    @Override
    protected void onEvent(WebsocketModel websocketModel) {
        mScreen.updateUI(websocketModel);
    }
}
