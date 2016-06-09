package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.ToolCommand;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;

public class SendToolCommand extends UseCaseInput<ToolCommand> {

    private final PrinterRepository mPrinterRepository;

    @Inject
    public SendToolCommand(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservableInput(ToolCommand toolCommand) {
        return mPrinterRepository.sendToolCommand(toolCommand);
    }
}
