package com.nairbspace.octoandroid.ui.terminal.console;

import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.EventPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class ConsolePresenter extends EventPresenter<ConsoleScreen, WebsocketModel> {

    private ConsoleScreen mScreen;

    @Inject
    public ConsolePresenter(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    protected void onInitialize(ConsoleScreen consoleScreen) {
        mScreen = consoleScreen;
    }

    @Override
    protected void onEvent(WebsocketModel model) {
        if (model.logs().isEmpty()) return;
        for (String log : model.logs()) {
            mScreen.updateUi(log);
        }
    }
}
