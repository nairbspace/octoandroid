package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.SlicingCommand;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;

public class SendSliceCommand extends UseCaseInput<SlicingCommand> {

    private final PrinterRepository mPrinterRepository;

    @Inject
    public SendSliceCommand(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservableInput(SlicingCommand slicingCommand) {
        return mPrinterRepository.sendSliceCommand(slicingCommand);
    }
}
