package com.nairbspace.octoandroid.ui.main;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetPrinterDetails;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.mapper.PrinterModelMapper;
import com.nairbspace.octoandroid.model.PrinterModel;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

public class MainPresenter extends UseCasePresenter<MainScreen> {

    private MainScreen mMainScreen;
    private final GetPrinterDetails mUseCase;
    private final PrinterModelMapper mPrinterModelMapper;

    @Inject
    public MainPresenter(GetPrinterDetails getPrinterDetailsUseCase,
                         PrinterModelMapper printerModelMapper) {
        super(getPrinterDetailsUseCase);
        mUseCase = getPrinterDetailsUseCase;
        mPrinterModelMapper = printerModelMapper;
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
            mPrinterModelMapper.execute(new TransformSubscriber(), printer);
        }
    }

    @RxLogSubscriber
    private final class TransformSubscriber extends DefaultSubscriber<PrinterModel> {

        @Override
        public void onNext(PrinterModel printerModel) {
            mMainScreen.updateNavHeader(printerModel.name(), printerModel.host());
        }
    }
}
