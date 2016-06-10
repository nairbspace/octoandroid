package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.ArbitraryCommand;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;

public class SendArbitraryCommand extends UseCaseInput<ArbitraryCommand> {

    private final PrinterRepository mPrinterRepository;

    @Inject
    public SendArbitraryCommand(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservableInput(ArbitraryCommand arbitraryCommand) {
        return mPrinterRepository.sendArbitraryCommand(arbitraryCommand);
    }
}
