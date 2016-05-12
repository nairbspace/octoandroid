package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;

public class DeletePrinterByName extends UseCaseInput<String> {

    private final PrinterRepository mPrinterRepository;

    @Inject
    public DeletePrinterByName(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservableInput(String s) {
        return mPrinterRepository.deletePrinterByName(s);
    }
}
