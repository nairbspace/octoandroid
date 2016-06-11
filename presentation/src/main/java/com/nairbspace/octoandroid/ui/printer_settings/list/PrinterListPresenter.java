package com.nairbspace.octoandroid.ui.printer_settings.list;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetPrinters;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.mapper.PrinterModelMapper;
import com.nairbspace.octoandroid.model.PrinterModel;
import com.nairbspace.octoandroid.ui.templates.UseCasePresenter;

import java.util.List;

import javax.inject.Inject;

public class PrinterListPresenter extends UseCasePresenter<PrinterListScreen> {

    private final GetPrinters mGetPrinters;
    private final PrinterModelMapper.ListMapper mListMapper;
    private PrinterListScreen mScreen;

    @Inject
    public PrinterListPresenter(GetPrinters getPrinters, PrinterModelMapper.ListMapper listMapper) {
        super(getPrinters);
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

    @Override
    protected void onDestroy(PrinterListScreen printerListScreen) {
        super.onDestroy(printerListScreen);
        mListMapper.unsubscribe();
    }
}
