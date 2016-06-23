package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.interactor.UseCaseInput;

import rx.Observable;

public abstract class MapperUseCase<I, O> extends UseCaseInput<I> {

    public MapperUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected abstract Observable<O> buildUseCaseObservableInput(final I i);
}
