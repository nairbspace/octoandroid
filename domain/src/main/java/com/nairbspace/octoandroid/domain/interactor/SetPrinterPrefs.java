package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;

public class SetPrinterPrefs extends UseCaseInput<Printer> {

    private final PrinterRepository mPrinterRepository;

    @Inject
    public SetPrinterPrefs(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservableInput(Printer printer) {
        return mPrinterRepository.setPrinterPrefs(printer);
    }
}
