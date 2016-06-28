package com.nairbspace.octoandroid.ui.templates;

import com.nairbspace.octoandroid.data.exception.NetworkConnectionException;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetPrinterDetails;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.mapper.PrinterModelMapper;
import com.nairbspace.octoandroid.model.PrinterModel;

import javax.inject.Inject;

import timber.log.Timber;

public class NavPresenter extends UseCasePresenter<NavScreen> {

    private final GetPrinterDetails mGetPrinterDetails;
    private final PrinterModelMapper mPrinterModelMapper;
    private NavScreen mScreen;

    @Inject
    public NavPresenter(GetPrinterDetails getPrinterDetails, PrinterModelMapper printerModelMapper) {
        super(getPrinterDetails, printerModelMapper);
        mGetPrinterDetails = getPrinterDetails;
        mPrinterModelMapper = printerModelMapper;
    }

    @Override
    protected void onInitialize(NavScreen navigatorScreen) {
        mScreen = navigatorScreen;
        execute();
    }

    @Override
    protected void onNetworkSwitched() {
        mScreen.hideSnackbar();
        execute();
    }

    @Override
    protected void execute() {
        mGetPrinterDetails.execute(new PrinterDetailsSubscriber());
    }

    @Override
    protected void networkNowInactive() {
        super.networkNowInactive();
        unsubscribeAll();
        String message = ErrorMessageFactory.create(mScreen.context(), new NetworkConnectionException());
        mScreen.displaySnackBar(message);
    }

    protected final class PrinterDetailsSubscriber extends DefaultSubscriber<Printer> {

        @Override
        public void onError(Throwable e) {
            if (ErrorMessageFactory.isThereNoActivePrinter(e)) {
                mScreen.displayNoActivePrinterSnackBar();
            } else {
                Timber.e(e, null);
            }
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
