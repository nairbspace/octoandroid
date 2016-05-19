package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

public class SendJobCommand extends UseCaseInput<HashMap<String, String>> {

    private final PrinterRepository mPrinterRepository;

    @Inject
    public SendJobCommand(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                          PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservableInput(HashMap<String, String> command) {
        return mPrinterRepository.sendJobCommand(command);
    }
}
