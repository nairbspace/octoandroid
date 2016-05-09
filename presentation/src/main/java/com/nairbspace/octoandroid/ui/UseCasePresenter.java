package com.nairbspace.octoandroid.ui;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.UseCase;

/**
 *
 * @param <T> Screen object
 * @param <U> Class that extends from DefaultSubscriber
 */
public abstract class UseCasePresenter<T> extends Presenter<T> {

    private final UseCase mUseCase;

    public UseCasePresenter(UseCase useCase) {
        mUseCase = useCase;
    }

    @Override
    protected void onDestroy(T t) {
        super.onDestroy(t);
        mUseCase.unsubscribe();
    }
}
