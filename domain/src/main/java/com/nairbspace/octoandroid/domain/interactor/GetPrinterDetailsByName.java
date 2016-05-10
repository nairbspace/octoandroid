package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.exception.NullUseCaseBuilderException;
import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetPrinterDetailsByName extends UseCase {

    private final PrinterRepository mPrinterRepository;
    private String mName;

    @Inject
    public GetPrinterDetailsByName(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (mName == null) {
            Observable.error(new NullUseCaseBuilderException());
        }
        return mPrinterRepository.printerDetails(mName);
    }

    public void setName(String name) {
        mName = name;
    }
}
