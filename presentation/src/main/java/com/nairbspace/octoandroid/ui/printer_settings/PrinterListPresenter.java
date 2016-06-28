package com.nairbspace.octoandroid.ui.printer_settings;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.DeletePrinterById;
import com.nairbspace.octoandroid.domain.interactor.GetPrinters;
import com.nairbspace.octoandroid.domain.interactor.SetActivePrinter;
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
    private final GetPrinters mGetPrinters;
    private final SetActivePrinter mSetActivePrinter;
    private final DeletePrinterById mDeletePrinterById;
    private PrinterListScreen mScreen;

    @Inject
    public PrinterListPresenter(GetPrinters getPrinters,
                                PrinterModelMapper.ListMapper listMapper,
                                SetPrinterPrefs setPrinterPrefs,
                                SetActivePrinter setActivePrinter,
                                DeletePrinterById deletePrinterById) {
        super(getPrinters, listMapper, setPrinterPrefs, setActivePrinter, deletePrinterById);
        mSetPrinterPrefs = setPrinterPrefs;
        mGetPrinters = getPrinters;
        mListMapper = listMapper;
        mSetActivePrinter = setActivePrinter;
        mDeletePrinterById = deletePrinterById;
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

    public void printerEditClicked(long id) {
        mSetPrinterPrefs.execute(new SetPrefsSubscriber(), id);
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

    public void printerDeleteClicked(long id, int position) {
        mDeletePrinterById.execute(new DeleteSubscriber(position), id);
    }

    private final class DeleteSubscriber extends DefaultSubscriber {

        private final int mPosition;

        private DeleteSubscriber(int position) {
            mPosition = position;
        }

        @Override
        public void onCompleted() {
            mScreen.deleteFromAdapter(mPosition);
        }
    }

    public void printerSetActiveClicked(long id) {
        mSetActivePrinter.execute(new SetActiveSubscriber(), id);
    }

    private final class SetActiveSubscriber extends DefaultSubscriber {

        @Override
        public void onCompleted() {
            mScreen.navigateToStatusActivity();
        }
    }
}
