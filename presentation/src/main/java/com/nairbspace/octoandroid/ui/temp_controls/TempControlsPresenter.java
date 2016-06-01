package com.nairbspace.octoandroid.ui.temp_controls;

import android.text.TextUtils;

import com.nairbspace.octoandroid.domain.interactor.GetPrinterDetails;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.UseCaseEventPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class TempControlsPresenter extends UseCaseEventPresenter<TempControlsScreen, WebsocketModel> {

    private TempControlsScreen mScreen;

    @Inject
    public TempControlsPresenter(GetPrinterDetails useCase, EventBus eventBus) {
        super(useCase, eventBus);
    }

    @Override
    protected void onEvent(WebsocketModel websocketModel) {
        if (!TextUtils.isEmpty(websocketModel.tempTime())) {
            mScreen.updateUi(websocketModel);
        }
    }

    @Override
    protected void execute() {
        super.execute();
    }

    @Override
    protected void onInitialize(TempControlsScreen tempControlsScreen) {
        mScreen = tempControlsScreen;
    }
}
