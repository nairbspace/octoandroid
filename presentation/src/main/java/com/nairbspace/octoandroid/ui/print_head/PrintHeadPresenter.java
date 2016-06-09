package com.nairbspace.octoandroid.ui.print_head;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.SendPrintHeadCommand;
import com.nairbspace.octoandroid.domain.model.PrintHeadCommand;
import com.nairbspace.octoandroid.ui.templates.UseCasePresenter;

import javax.inject.Inject;

public class PrintHeadPresenter extends UseCasePresenter<PrintHeadScreen> {

    private final SendPrintHeadCommand mSendPrintHeadCommand;
    private PrintHeadScreen mScreen;

    @Inject
    public PrintHeadPresenter(SendPrintHeadCommand printHeadCommand) {
        super(printHeadCommand);
        mSendPrintHeadCommand = printHeadCommand;
    }

    @Override
    protected void onInitialize(PrintHeadScreen printHeadScreen) {
        mScreen = printHeadScreen;
    }

    public void executeCommand(PrintHeadCommand.Type type) {
        PrintHeadCommand printHeadCommand = null;
        switch (type) {
            case JOG_X_RIGHT:
            case JOG_Y_UP:
            case JOG_Z_UP:
                printHeadCommand = PrintHeadCommand.createJog(type, mScreen.getJogMultiplier());
                break;
            case JOG_X_LEFT:
            case JOG_Y_DOWN:
            case JOG_Z_DOWN:
                printHeadCommand = PrintHeadCommand.createJog(type, mScreen.getJogMultiplier() * -1);
                break;
            case HOME_XY:
            case HOME_Z:
                printHeadCommand = PrintHeadCommand.createHome(type);
                break;
            case FEEDRATE:
                printHeadCommand = PrintHeadCommand.createFeedRate(type, mScreen.getFeedRateWithOffset());
                break;

        }

        mSendPrintHeadCommand.execute(new CommandSubscriber(), printHeadCommand);
    }

    @Override
    protected void networkNowInactive() {
        mScreen.setEnableViews(false);
    }

    @Override
    protected void onNetworkSwitched() {
        mScreen.setEnableViews(true);
    }

    private final class CommandSubscriber extends DefaultSubscriber {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mScreen.showToast("Error sending command"); // TODO-low reference from error message factory
        }

        @Override
        public void onCompleted() {
            mScreen.showToast("Command sent");
        }
    }
}
