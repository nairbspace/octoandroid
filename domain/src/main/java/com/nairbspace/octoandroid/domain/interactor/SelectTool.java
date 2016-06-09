package com.nairbspace.octoandroid.domain.interactor;

import android.support.annotation.NonNull;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;

public class SelectTool extends UseCaseInput<Integer> {

    private final PrinterRepository mPrinterRepository;

    @Inject
    public SelectTool(ThreadExecutor threadExecutor,
                      PostExecutionThread postExecutionThread,
                      PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservableInput(@NonNull Integer integer) {
        return mPrinterRepository.selectTool(integer);
    }
}
