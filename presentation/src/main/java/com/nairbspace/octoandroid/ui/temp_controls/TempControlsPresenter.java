package com.nairbspace.octoandroid.ui.temp_controls;

import android.text.TextUtils;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.SendTempCommand;
import com.nairbspace.octoandroid.domain.model.TempCommand;
import com.nairbspace.octoandroid.domain.model.TempCommand.ToolBedOffsetTemp;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.UseCaseEventPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class TempControlsPresenter extends UseCaseEventPresenter<TempControlsScreen, WebsocketModel> {

    private TempControlsScreen mScreen;
    private final SendTempCommand mSendTempCommand;

    @Inject
    public TempControlsPresenter(SendTempCommand sendTempCommand, EventBus eventBus) {
        super(sendTempCommand, eventBus);
        mSendTempCommand = sendTempCommand;
    }

    @Override
    protected void onEvent(WebsocketModel websocketModel) {
        if (!TextUtils.isEmpty(websocketModel.tempTime())) {
            mScreen.updateUi(websocketModel);
        }
    }

    public void execute(ToolBedOffsetTemp toolBedOffsetTemp, String stringTemp) {
        if (TextUtils.isEmpty(stringTemp)) {
            mScreen.inputError(toolBedOffsetTemp, "Cannot be blank"); // TODO incorporate errors in error factory
            return;
        }

        int temp;
        try {
            temp = Integer.parseInt(stringTemp);
        } catch (NumberFormatException e) {
            mScreen.inputError(toolBedOffsetTemp, "Input error");
            return;
        }

        mScreen.hideSoftKeyboard();
        TempCommand tempCommand = TempCommand.create(toolBedOffsetTemp, temp);
        mSendTempCommand.execute(new TempCommandSubscriber(), tempCommand);
    }

    @Override
    protected void networkNowActive() {
        mScreen.enableButtons(true);
    }

    @Override
    protected void networkNowInactive() {
        mScreen.enableButtons(false);
        // TODO kill subscription
    }

    @Override
    protected void onInitialize(TempControlsScreen tempControlsScreen) {
        mScreen = tempControlsScreen;
    }

    public final class TempCommandSubscriber extends DefaultSubscriber {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mScreen.toastMessage("Error sending command");
        }

        @Override
        public void onNext(Object o) {
            mScreen.toastMessage("Command sent");
        }
    }
}
