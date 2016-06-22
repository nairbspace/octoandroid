package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.exception.NullUseCaseBuilderException;
import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public abstract class UseCaseInput<I> extends UseCase {

    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;
    private Subscription mSubscription = Subscriptions.unsubscribed();

    public UseCaseInput(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    protected abstract Observable buildUseCaseObservableInput(final I i);

    @SuppressWarnings("unchecked")
    public void execute(Subscriber useCaseSubscriber, I i) {
        unsubscribe();
        mSubscription = buildUseCaseObservableInput(i)
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(useCaseSubscriber);
    }

    @SuppressWarnings("unchecked")
    public void executeAllBg(Subscriber useCaseSubscriber, I i) {
        unsubscribe();
        mSubscription = buildUseCaseObservableInput(i)
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(Schedulers.from(mThreadExecutor))
                .subscribe(useCaseSubscriber);
    }

    @SuppressWarnings("unchecked")
    public void executeUnsubBg(Subscriber useCaseSubscriber, I i) {
        unsubscribe();
        mSubscription = buildUseCaseObservableInput(i)
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler())
                .unsubscribeOn(Schedulers.from(mThreadExecutor))
                .subscribe(useCaseSubscriber);
    }

    @Override
    public void unsubscribe() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    /**
     * Do not use this if extending from this class
     * @param useCaseSubscriber the subscriber class
     */
    @Deprecated
    @Override
    public void execute(Subscriber useCaseSubscriber) {
        throw new RuntimeException(new NullUseCaseBuilderException());
    }

    /**
     * Do not use this if extending from this class
     * @param useCaseSubscriber the subscriber class
     */
    @Deprecated
    @Override
    public void executeAllBg(Subscriber useCaseSubscriber) {
        throw new RuntimeException(new NullUseCaseBuilderException());
    }

    /**
     * Do not use this if extending from this class
     * @param useCaseSubscriber the subscriber class
     */
    @Deprecated
    @Override
    public void executeUnsubBg(Subscriber useCaseSubscriber) {
        throw new RuntimeException(new NullUseCaseBuilderException());
    }

    /**
     * Do not use this if extending from this class
     * @return throws exception
     */
    @Deprecated
    @Override
    protected Observable buildUseCaseObservable() {
        throw new RuntimeException(new NullUseCaseBuilderException());
    }
}
