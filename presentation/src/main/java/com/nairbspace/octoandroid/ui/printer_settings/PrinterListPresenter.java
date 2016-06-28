package com.nairbspace.octoandroid.ui.printer_settings;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetPrinters;
import com.nairbspace.octoandroid.domain.interactor.SetPrinterPrefs;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.mapper.PrinterModelMapper;
import com.nairbspace.octoandroid.model.PrinterModel;
import com.nairbspace.octoandroid.ui.templates.UseCasePresenter;

import java.util.List;

import javax.inject.Inject;

public class PrinterListPresenter extends UseCasePresenter<PrinterListScreen> {

    private final PrinterModelMapper.ListMapper mListMapper;
    private final SetPrinterPrefs mSetPrinterPrefs;
    private final PrinterModelMapper.DomainMapper mDomainMapper;
    private final GetPrinters mGetPrinters;
    private PrinterListScreen mScreen;

    @Inject
    public PrinterListPresenter(GetPrinters getPrinters,
                                PrinterModelMapper.ListMapper listMapper,
                                SetPrinterPrefs setPrinterPrefs,
                                PrinterModelMapper.DomainMapper domainMapper) {
        super(getPrinters, listMapper, setPrinterPrefs, domainMapper);
        mSetPrinterPrefs = setPrinterPrefs;
        mDomainMapper = domainMapper;
        mGetPrinters = getPrinters;
        mListMapper = listMapper;
    }

    @Override
    protected void onInitialize(PrinterListScreen printerListScreen) {
        mScreen = printerListScreen;
        execute();
    }

    @Override
    protected void execute() {
        mGetPrinters.execute(new ListSubscriber());
    }

    private final class ListSubscriber extends DefaultSubscriber<List<Printer>> {
        @Override
        public void onError(Throwable e) {
            super.onError(e); // TODO error handling
        }

        @Override
        public void onNext(List<Printer> printers) {
            mListMapper.execute(new MapperSubscriber(), printers);
        }
    }

    private final class MapperSubscriber extends DefaultSubscriber<List<PrinterModel>> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(List<PrinterModel> printerModels) {
            mScreen.updateUi(printerModels);
        }
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
