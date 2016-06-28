package com.nairbspace.octoandroid.ui.printer_settings;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.SetPrinterPrefs;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.mapper.PrinterModelMapper;
import com.nairbspace.octoandroid.model.PrinterModel;
import com.nairbspace.octoandroid.ui.templates.UseCasePresenter;

import javax.inject.Inject;

public class PrinterListActivityPresenter extends UseCasePresenter<PrinterListActivityScreen> {

    private final SetPrinterPrefs mSetPrinterPrefs;
    private final PrinterModelMapper.DomainMapper mDomainMapper;
    private PrinterListActivityScreen mScreen;

    @Inject
    public PrinterListActivityPresenter(SetPrinterPrefs setPrinterPrefs,
                                        PrinterModelMapper.DomainMapper domainMapper) {
        super(setPrinterPrefs, domainMapper);
        mSetPrinterPrefs = setPrinterPrefs;
        mDomainMapper = domainMapper;
    }

    @Override
    protected void onInitialize(PrinterListActivityScreen printerListActivityScreen) {
        mScreen = printerListActivityScreen;
    }

    public void printerSettingsClicked(PrinterModel printerModel) {
        mDomainMapper.execute(new DomainMapperSubscriber(), printerModel);
    }

    private final class DomainMapperSubscriber extends DefaultSubscriber<Printer> {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Printer printer) {
            mSetPrinterPrefs.execute(new SetPrefsSubscriber(), printer);
        }
    }

    private final class SetPrefsSubscriber extends DefaultSubscriber {

        @Override
        public void onCompleted() {
            mScreen.navigateToPrinterDetailsActivity();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }
    }
}
