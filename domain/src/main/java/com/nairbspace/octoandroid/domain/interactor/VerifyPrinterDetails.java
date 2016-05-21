package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;

public class VerifyPrinterDetails extends UseCaseInput<AddPrinter> {
    private final PrinterRepository mPrinterRepository;

    @Inject
    public VerifyPrinterDetails(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservableInput(AddPrinter addPrinter) {
        return mPrinterRepository.verifyPrinterDetails(addPrinter);
    }
}
