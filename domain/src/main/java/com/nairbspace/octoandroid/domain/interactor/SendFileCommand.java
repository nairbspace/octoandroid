package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.FileCommand;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;

public class SendFileCommand extends UseCaseInput<FileCommand> {

    private final PrinterRepository mPrinterRepository;

    @Inject
    public SendFileCommand(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservableInput(FileCommand fileCommand) {
        return mPrinterRepository.sendFileCommand(fileCommand);
    }
}
