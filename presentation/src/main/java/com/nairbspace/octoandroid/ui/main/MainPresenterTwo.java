package com.nairbspace.octoandroid.ui.main;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.nairbspace.octoandroid.domain.Printer;
import com.nairbspace.octoandroid.domain.exception.DefaultErrorBundle;
import com.nairbspace.octoandroid.domain.exception.ErrorBundle;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.UseCase;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.ui.Presenter;

import javax.inject.Inject;

public class MainPresenterTwo extends Presenter<MainScreen> {

    private MainScreen mMainScreen;
    private UseCase mGetPrinterDetailsUseCase;

    @Inject
    public MainPresenterTwo(UseCase getPrinterDetailsUseCase) {
        mGetPrinterDetailsUseCase = getPrinterDetailsUseCase;
    }

    @Override
    protected void onInitialize(MainScreen mainScreen) {
        mMainScreen = mainScreen;
        mGetPrinterDetailsUseCase.execute(new PrinterDetailsSubscriber());
    }

    private void success() {
        mMainScreen.displaySnackBar("Subscriber success");
    }

    private void failure(ErrorBundle errorBundle) {
        if (ErrorMessageFactory.isThereNoActivePrinter(errorBundle.getException())) {
            mMainScreen.displaySnackBar("No Active Printer");
        }
    }

    @RxLogSubscriber
    private final class PrinterDetailsSubscriber extends DefaultSubscriber<Printer> {

        @Override
        public void onCompleted() {
            success();
        }

        @Override
        public void onError(Throwable e) {
            failure(new DefaultErrorBundle((Exception) e));
        }

        @Override
        public void onNext(Printer printer) {
        }
    }
}
