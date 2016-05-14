package com.nairbspace.octoandroid.ui;

import com.nairbspace.octoandroid.domain.interactor.UseCase;

/**
 * @param <T> Screen object
 */
public abstract class UseCasePresenter<T> extends Presenter<T> {

    private final UseCase mUseCase;

    public UseCasePresenter(UseCase useCase) {
        mUseCase = useCase;
    }

    protected void execute() {
    }

    @Override
    protected void onDestroy(T t) {
        super.onDestroy(t);
        mUseCase.unsubscribe();
    }
}
