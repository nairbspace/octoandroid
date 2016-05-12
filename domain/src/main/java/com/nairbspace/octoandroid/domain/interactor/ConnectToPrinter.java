package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;

public class ConnectToPrinter extends UseCaseInput<Connect> {

    private final PrinterRepository mPrinterRepository;

    @Inject
    public ConnectToPrinter(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservableInput(Connect connect) {
        return mPrinterRepository.connectToPrinter(connect);
    }
}
