package com.nairbspace.octoandroid.ui.printer_controls_tool;

import android.text.TextUtils;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.SelectTool;
import com.nairbspace.octoandroid.domain.interactor.SendToolCommand;
import com.nairbspace.octoandroid.domain.model.ToolCommand;
import com.nairbspace.octoandroid.ui.templates.UseCasePresenter;

import javax.inject.Inject;

public class ToolPresenter extends UseCasePresenter<ToolScreen> {

    private final SelectTool mSelectTool;
    private final SendToolCommand mSendToolCommand;
    private ToolScreen mScreen;

    @Inject
    public ToolPresenter(SelectTool selectTool, SendToolCommand sendToolCommand) {
        super(selectTool, sendToolCommand);
        mSelectTool = selectTool;
        mSendToolCommand = sendToolCommand;
    }

    @Override
    protected void onInitialize(ToolScreen toolScreen) {
        mScreen = toolScreen;
    }

    @Override
    protected void networkNowInactive() {
        mScreen.setEnableViews(false);
    }

    @Override
    protected void onNetworkSwitched() {
        mScreen.setEnableViews(true);
    }

    public void executeSelectTool(ToolCommand.Type type) {
        mSelectTool.execute(new ToolSubscriber(type), mScreen.getTool());
    }

    private final class ToolSubscriber extends DefaultSubscriber {

        private final ToolCommand.Type mType;

        private ToolSubscriber(ToolCommand.Type type) {
            mType = type;
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mScreen.showToast("Error selecting tool");
        }

        @Override
        public void onCompleted() {
            if (mType != ToolCommand.Type.FLOWRATE) {
                executeAmount(mType);
            } else {
                executeFlowrate();
            }
        }
    }

    public void executeAmount(ToolCommand.Type type) {
        String s = mScreen.getAmount();
        if (TextUtils.isEmpty(s)) {
            mScreen.showInputAmountError("Cannot be empty");
            return;
        }

        int amount = 0;
        try {
            amount = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            mScreen.showInputAmountError("Incorrect formatting");
        }

        if (amount < 1) {
            mScreen.showInputAmountError("Brb extruding nothing");
            return;
        }

        switch (type) {
            case RETRACT:
                executeCommand(ToolCommand.createRetract(amount));
                break;
            case EXTRACT:
                executeCommand(ToolCommand.createExtract(amount));
        }
    }

    public void executeFlowrate() {
        executeCommand(ToolCommand.createFlowrate(mScreen.getFlowrateWithOffset()));
    }

    public void executeCommand(ToolCommand toolCommand) {
        mSendToolCommand.execute(new ToolCommandSubscriber(), toolCommand);
    }

    private final class ToolCommandSubscriber extends DefaultSubscriber {
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
