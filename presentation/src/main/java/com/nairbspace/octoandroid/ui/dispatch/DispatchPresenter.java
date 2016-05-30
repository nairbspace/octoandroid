package com.nairbspace.octoandroid.ui.dispatch;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetPrinterDetails;
import com.nairbspace.octoandroid.ui.templates.UseCasePresenter;

import javax.inject.Inject;

public class DispatchPresenter extends UseCasePresenter<DispatchScreen> {

    private final GetPrinterDetails mGetPrinterDetails;
    private DispatchScreen mScreen;

    @Inject
    public DispatchPresenter(GetPrinterDetails getPrinterDetails) {
        super(getPrinterDetails);
        mGetPrinterDetails = getPrinterDetails;
    }

    @Override
    protected void onInitialize(DispatchScreen dispatchScreen) {
        mScreen = dispatchScreen;
        execute();
    }

    @Override
    protected void execute() {
        mGetPrinterDetails.execute(new PrinterDetailsSubscriber());
    }

    private final class PrinterDetailsSubscriber extends DefaultSubscriber {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mScreen.navigateToAddPrinterActivityForResult();
        }

        @Override
        public void onCompleted() {
            mScreen.navigateToStatusActivity();
        }
    }
}
