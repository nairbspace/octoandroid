package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.PrintHeadCommand;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;

public class SendPrintHeadCommand extends UseCaseInput<PrintHeadCommand> {

    private final PrinterRepository mPrinterRepository;

    @Inject
    public SendPrintHeadCommand(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservableInput(PrintHeadCommand printHeadCommand) {
        return mPrinterRepository.sendPrintHeadCommand(printHeadCommand);
    }
}
