package com.nairbspace.octoandroid.ui.dispatch;

import com.nairbspace.octoandroid.domain.interactor.CheckIfDbIsEmpty;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.ui.templates.UseCasePresenter;

import javax.inject.Inject;

public class DispatchPresenter extends UseCasePresenter<DispatchScreen> {

    private final CheckIfDbIsEmpty mCheckIfDbIsEmpty;
    private DispatchScreen mScreen;

    @Inject
    public DispatchPresenter(CheckIfDbIsEmpty checkIfDbIsEmpty) {
        super(checkIfDbIsEmpty);
        mCheckIfDbIsEmpty = checkIfDbIsEmpty;
    }

    @Override
    protected void onInitialize(DispatchScreen dispatchScreen) {
        mScreen = dispatchScreen;
        execute();
    }

    @Override
    protected void execute() {
        mCheckIfDbIsEmpty.execute(new DbEmptySubscriber());
    }

    private final class DbEmptySubscriber extends DefaultSubscriber<Boolean> {

        @Override
        public void onNext(Boolean aBoolean) {
            if (aBoolean) {
                mScreen.navigateToAddPrinterActivityForResult();
            } else {
                mScreen.navigateToStatusActivity();
            }
        }
    }
}
