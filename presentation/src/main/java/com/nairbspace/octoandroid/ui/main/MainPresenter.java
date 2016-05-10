package com.nairbspace.octoandroid.ui.main;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.nairbspace.octoandroid.domain.Printer;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetPrinterDetails;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.model.PrinterModel;
import com.nairbspace.octoandroid.model.mapper.DomainMapper;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

import rx.functions.Action1;

public class MainPresenter extends UseCasePresenter<MainScreen> {

    private MainScreen mMainScreen;
    private final GetPrinterDetails mUseCase;
    private final DomainMapper mDomainMapper;

    @Inject
    public MainPresenter(GetPrinterDetails getPrinterDetailsUseCase, DomainMapper domainMapper) {
        super(getPrinterDetailsUseCase);
        mUseCase = getPrinterDetailsUseCase;
        mDomainMapper = domainMapper;
    }

    @Override
    protected void onInitialize(MainScreen mainScreen) {
        mMainScreen = mainScreen;
        execute();
    }

    public void execute() {
        mUseCase.execute(new PrinterDetailsSubscriber());
    }

    @RxLogSubscriber
    protected final class PrinterDetailsSubscriber extends DefaultSubscriber<Printer> {

        @Override
        public void onError(Throwable e) {
            Exception exception = (Exception) e;
            if (ErrorMessageFactory.isThereNoActivePrinter(exception)) {
                mMainScreen.navigateToAddPrinterActivityForResult();
            } else {
                String error = ErrorMessageFactory.create(mMainScreen.context(), exception);
                mMainScreen.displaySnackBar(error);
            }
        }

        @Override
        public void onNext(Printer printer) {
            mDomainMapper.transformObservable(printer).subscribe(new Action1<PrinterModel>() {
                @Override
                public void call(PrinterModel printerModel) {
                    mMainScreen.updateNavHeader(printerModel.name(), printerModel.host());
                }
            });
        }
    }
}
