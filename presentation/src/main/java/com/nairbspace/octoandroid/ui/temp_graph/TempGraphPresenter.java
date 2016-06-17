package com.nairbspace.octoandroid.ui.temp_graph;

import android.text.TextUtils;

import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.EventPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class TempGraphPresenter extends EventPresenter<TempGraphScreen, WebsocketModel> {

    private TempGraphScreen mScreen;
    private String mPreviousTime;

    @Inject public TempGraphPresenter(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    protected void onInitialize(TempGraphScreen tempGraphScreen) {
        mScreen = tempGraphScreen;
    }

    @Override
    protected void onEvent(WebsocketModel websocketModel) {
        String tempTime = websocketModel.tempTime();
        if (!TextUtils.isEmpty(tempTime) && !isTempTimeStale(tempTime)) {
            mScreen.updateUi(websocketModel);
        }
    }

    private boolean isTempTimeStale(String tempTime) {
        if (mPreviousTime == null) {
            mPreviousTime = tempTime;
            return false;
        }

        if (!mPreviousTime.equals(tempTime)) {
            mPreviousTime = tempTime;
            return false;
        }

        return true;
    }
}
