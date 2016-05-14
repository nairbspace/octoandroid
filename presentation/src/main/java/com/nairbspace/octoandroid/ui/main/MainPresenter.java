package com.nairbspace.octoandroid.ui.main;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.nairbspace.octoandroid.data.exception.NetworkConnectionException;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetPrinterDetails;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.mapper.PrinterModelMapper;
import com.nairbspace.octoandroid.model.PrinterModel;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

public class MainPresenter extends UseCasePresenter<MainScreen> {

    private MainScreen mScreen;
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
        mScreen = mainScreen;
        execute();
    }

    @Override
    protected void onNetworkSwitched() {
        super.onNetworkSwitched();
        mScreen.hideSnackbar();
    }

    @Override
    protected void networkNowInactive() {
        super.networkNowInactive();
        String message = ErrorMessageFactory.create(mScreen.context(), new NetworkConnectionException());
        mScreen.displaySnackBar(message);
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
                mScreen.navigateToAddPrinterActivityForResult();
            } else {
                String error = ErrorMessageFactory.create(mScreen.context(), exception);
                mScreen.displaySnackBar(error);
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
            mScreen.updateNavHeader(printerModel.name(), printerModel.host());
        }
    }
}
