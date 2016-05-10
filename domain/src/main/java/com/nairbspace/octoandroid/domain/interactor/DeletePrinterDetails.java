package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.pojo.Printer;
import com.nairbspace.octoandroid.domain.exception.NullUseCaseBuilderException;
import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;

public class DeletePrinterDetails extends UseCase {

    private final PrinterRepository mPrinterRepository;
    private Printer mPrinter;

    @Inject
    public DeletePrinterDetails(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (mPrinter == null) {
            return Observable.error(new NullUseCaseBuilderException());
        }
        return mPrinterRepository.deletePrinterDetails(mPrinter);
    }

    public void setPrinter(Printer printer) {
        mPrinter = printer;
    }
}
