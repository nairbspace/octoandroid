package com.nairbspace.octoandroid.ui.temp_controls;

import com.nairbspace.octoandroid.domain.interactor.GetPrinterDetails;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.UseCaseEventPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class TempControlsPresenter extends UseCaseEventPresenter<TempControlsScreen, WebsocketModel> {

    @Inject
    public TempControlsPresenter(GetPrinterDetails useCase, EventBus eventBus) {
        super(useCase, eventBus);
    }

    @Override
    protected void onEvent(WebsocketModel websocketModel) {

    }

    @Override
    protected void onInitialize(TempControlsScreen tempControlsScreen) {

    }
}
