package com.nairbspace.octoandroid.ui.printer_controls_general;

import android.support.annotation.Nullable;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.SendArbitraryCommand;
import com.nairbspace.octoandroid.domain.model.ArbitraryCommand;
import com.nairbspace.octoandroid.ui.templates.UseCasePresenter;

import java.util.List;

import javax.inject.Inject;

public class GeneralPresenter extends UseCasePresenter<GeneralScreen> {

    private final SendArbitraryCommand mSendArbitraryCommand;
    private GeneralScreen mScreen;

    @Inject
    public GeneralPresenter(SendArbitraryCommand sendArbitraryCommand) {
        super(sendArbitraryCommand);
        mSendArbitraryCommand = sendArbitraryCommand;
    }

    @Override
    protected void onInitialize(GeneralScreen generalScreen) {
        mScreen = generalScreen;
    }

    @Override
    protected void networkNowInactive() {
        mScreen.setEnableViews(false);
    }

    @Override
    protected void onNetworkSwitched() {
        mScreen.setEnableViews(true);
    }

    public void executeCommand(ArbitraryCommand.Type type, @Nullable String command, @Nullable List<String> commands) {
        ArbitraryCommand arbitraryCommand = null;
        switch (type) {
            case MOTORS_OFF:
            case FAN_ON:
            case FAN_OFF:
                arbitraryCommand = ArbitraryCommand.createGeneral(type);
                break;
            case SINGLE:
                arbitraryCommand = ArbitraryCommand.createSingle(type, command);
                break;
            case MULTIPLE:
                arbitraryCommand = ArbitraryCommand.createMultiple(type, commands);
                break;
        }

        mSendArbitraryCommand.execute(new CommandSubscriber(), arbitraryCommand);
    }

    private final class CommandSubscriber extends DefaultSubscriber {

        @Override
        public void onCompleted() {
            mScreen.showToast("Command sent");
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mScreen.showToast("Error sending command");
        }
    }
}
