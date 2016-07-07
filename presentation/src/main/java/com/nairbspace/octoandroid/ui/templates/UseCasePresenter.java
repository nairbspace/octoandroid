package com.nairbspace.octoandroid.ui.templates;

import com.nairbspace.octoandroid.domain.interactor.UseCase;

/**
 * @param <T> Screen object
 */
public abstract class UseCasePresenter<T> extends Presenter<T> {

    private final UseCase[] mUseCases;

    public UseCasePresenter(UseCase... useCases) {
        mUseCases = useCases;
    }

    protected void execute() {
    }

    @Override
    protected void onDestroy(T t) {
        unsubscribeAll();
        super.onDestroy(t);
    }

    protected void unsubscribeAll() {
        for (UseCase useCase : mUseCases) {
            useCase.unsubscribe();
        }
    }
}
