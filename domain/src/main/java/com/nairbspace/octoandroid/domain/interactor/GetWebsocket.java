package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class GetWebsocket extends UseCase {

    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;
    private Subscription mSubscription = Subscriptions.unsubscribed();
    private final PrinterRepository mPrinterRepository;

    @Inject
    public GetWebsocket(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                        PrinterRepository printerRepository) {
        super(threadExecutor, postExecutionThread);
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
        mPrinterRepository = printerRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return mPrinterRepository.getWebsocket().retry();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Subscriber useCaseSubscriber) {
        unsubscribe();
        mSubscription = buildUseCaseObservable()
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler())
                .unsubscribeOn(Schedulers.from(mThreadExecutor)) // TODO hotfix till unsubscribe problem is solved
                .subscribe(useCaseSubscriber);
    }

    @Override
    public void unsubscribe() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
