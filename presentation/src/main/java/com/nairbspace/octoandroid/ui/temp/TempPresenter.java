package com.nairbspace.octoandroid.ui.temp;

import android.text.TextUtils;

import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.EventPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class TempPresenter extends EventPresenter<TempScreen, WebsocketModel> {

    private TempScreen mScreen;

    @Inject public TempPresenter(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    protected void onInitialize(TempScreen tempScreen) {
        mScreen = tempScreen;
    }

    @Override
    protected void onEvent(WebsocketModel websocketModel) {
        if (!TextUtils.isEmpty(websocketModel.tempTime())) {
            mScreen.updateUi(websocketModel);
        }
    }
}
