package com.nairbspace.octoandroid.ui.temp_graph;

import android.text.TextUtils;

import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.EventPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class TempGraphPresenter extends EventPresenter<TempGraphScreen, WebsocketModel> {

    private TempGraphScreen mScreen;

    @Inject public TempGraphPresenter(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    protected void onInitialize(TempGraphScreen tempGraphScreen) {
        mScreen = tempGraphScreen;
    }

    @Override
    protected void onEvent(WebsocketModel websocketModel) {
        if (!TextUtils.isEmpty(websocketModel.tempTime())) {
            mScreen.updateUi(websocketModel);
        }
    }
}
