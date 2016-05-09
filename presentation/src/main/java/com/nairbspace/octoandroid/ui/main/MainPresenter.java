package com.nairbspace.octoandroid.ui.main;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.nairbspace.octoandroid.domain.Printer;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetPrinterDetails;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

public class MainPresenter extends UseCasePresenter<MainScreen> {

    private MainScreen mMainScreen;
    private final GetPrinterDetails mUseCase;

    @Inject
    public MainPresenter(GetPrinterDetails getPrinterDetailsUseCase) {
        super(getPrinterDetailsUseCase);
        mUseCase = getPrinterDetailsUseCase;
    }

    @Override
    protected void onInitialize(MainScreen mainScreen) {
        mMainScreen = mainScreen;
        mUseCase.execute(new PrinterDetailsSubscriber());
    }

    @RxLogSubscriber
    protected final class PrinterDetailsSubscriber extends DefaultSubscriber<Printer> {

        @Override
        public void onCompleted() {
            mMainScreen.displaySnackBar("onComplete");
        }

        @Override
        public void onError(Throwable e) {
            Exception exception = (Exception) e;
            if (ErrorMessageFactory.isThereNoActivePrinter(exception)) {
                mMainScreen.navigateToAddPrinterActivity();
            } else {
                String error = ErrorMessageFactory.create(mMainScreen.context(), exception);
                mMainScreen.displaySnackBar(error);
            }
        }

        @Override
        public void onNext(Printer printer) {
            // TODO Transform and add nav data
        }
    }
}
