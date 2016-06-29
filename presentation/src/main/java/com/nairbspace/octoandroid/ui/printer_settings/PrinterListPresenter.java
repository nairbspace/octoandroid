package com.nairbspace.octoandroid.ui.printer_settings;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.DeletePrinterById;
import com.nairbspace.octoandroid.domain.interactor.GetPrinters;
import com.nairbspace.octoandroid.domain.interactor.SetActivePrinter;
import com.nairbspace.octoandroid.domain.interactor.SetPrinterPrefs;
import com.nairbspace.octoandroid.domain.interactor.VerifyPrinterEdit;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
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
    private final VerifyPrinterEdit mVerifyPrinterEdit;
    private PrinterListScreen mScreen;

    @Inject
    public PrinterListPresenter(GetPrinters getPrinters,
                                PrinterModelMapper.ListMapper listMapper,
                                SetPrinterPrefs setPrinterPrefs,
                                SetActivePrinter setActivePrinter,
                                DeletePrinterById deletePrinterById,
                                VerifyPrinterEdit verifyPrinterEdit) {
        super(getPrinters, listMapper, setPrinterPrefs,
                setActivePrinter, deletePrinterById, verifyPrinterEdit);
        mSetPrinterPrefs = setPrinterPrefs;
        mGetPrinters = getPrinters;
        mListMapper = listMapper;
        mSetActivePrinter = setActivePrinter;
        mDeletePrinterById = deletePrinterById;
        mVerifyPrinterEdit = verifyPrinterEdit;
    }

    @Override
    protected void onInitialize(PrinterListScreen printerListScreen) {
        mScreen = printerListScreen;
        execute();
    }

    @Override
    protected void execute() {
        mScreen.setRefreshing(true);
        mGetPrinters.execute(new ListSubscriber());
    }

    private final class ListSubscriber extends DefaultSubscriber<List<Printer>> {
        @Override
        public void onError(Throwable e) {
            super.onError(e); // TODO error handling
            mScreen.setRefreshing(false);
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
            mScreen.setRefreshing(false);
        }

        @Override
        public void onNext(List<PrinterModel> printerModels) {
            mScreen.setRefreshing(false);
            mScreen.updateUi(printerModels);
        }
    }

    public void printerEditClicked(long id, int position) {
        mSetPrinterPrefs.execute(new SetPrefsSubscriber(position), id);
    }

    private final class SetPrefsSubscriber extends DefaultSubscriber {

        private final int mPosition;

        private SetPrefsSubscriber(int position) {
            mPosition = position;
        }

        @Override
        public void onCompleted() {
            mScreen.navigateToPrinterDetailsActivity(mPosition);
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }
    }

    public void verifyEdit(int position) {
        mScreen.setRefreshing(true);
        mVerifyPrinterEdit.execute(new VerifyEditSubscriber(position));
    }

    private final class VerifyEditSubscriber extends DefaultSubscriber {

        private final int mPosition;

        private VerifyEditSubscriber(int position) {
            mPosition = position;
        }

        @Override
        public void onCompleted() {
            mScreen.setRefreshing(false);
            // TODO need nice way to update only item...
            execute();
        }

        @Override
        public void onError(Throwable e) {
            mScreen.setRefreshing(false);
            if (ErrorMessageFactory.doesPrinterNameExists(e)) {
                mScreen.showNameExists();
            } else {
                mScreen.showEditFailure();
            }
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
