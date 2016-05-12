package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.exception.NullUseCaseBuilderException;
import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;

public class ConnectToPrinter extends UseCase {

    private final PrinterRepository mPrinterRepository;
    private Connect mConnect;

    @Inject
    public ConnectToPrinter(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (mConnect == null) {
            return Observable.error(new NullUseCaseBuilderException());
        }
        return mPrinterRepository.connectToPrinter(mConnect);
    }

    public void setConnect(Connect connect) {
        mConnect = connect;
    }
}
