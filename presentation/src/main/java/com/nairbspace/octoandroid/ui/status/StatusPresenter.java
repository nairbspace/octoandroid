package com.nairbspace.octoandroid.ui.status;

import com.nairbspace.octoandroid.data.exception.NetworkConnectionException;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetPrinterDetails;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.mapper.PrinterModelMapper;
import com.nairbspace.octoandroid.model.PrinterModel;
import com.nairbspace.octoandroid.ui.templates.UseCasePresenter;

import javax.inject.Inject;

public class StatusPresenter extends UseCasePresenter<StatusScreen> {

    private StatusScreen mScreen;
    private final GetPrinterDetails mUseCase;
    private final PrinterModelMapper mPrinterModelMapper;

    @Inject
    public StatusPresenter(GetPrinterDetails getPrinterDetailsUseCase,
                           PrinterModelMapper printerModelMapper) {
        super(getPrinterDetailsUseCase);
        mUseCase = getPrinterDetailsUseCase;
        mPrinterModelMapper = printerModelMapper;
    }

    @Override
    protected void onInitialize(StatusScreen statusScreen) {
        mScreen = statusScreen;
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

    protected final class PrinterDetailsSubscriber extends DefaultSubscriber<Printer> {

        @Override
        public void onError(Throwable e) {
            Exception exception = (Exception) e;
            String error = ErrorMessageFactory.create(mScreen.context(), exception);
            mScreen.displaySnackBar(error);
        }

        @Override
        public void onNext(Printer printer) {
            mPrinterModelMapper.execute(new TransformSubscriber(), printer);
        }
    }

    private final class TransformSubscriber extends DefaultSubscriber<PrinterModel> {

        @Override
        public void onNext(PrinterModel printerModel) {
            mScreen.updateNavHeader(printerModel.name(), printerModel.host());
        }
    }
}
