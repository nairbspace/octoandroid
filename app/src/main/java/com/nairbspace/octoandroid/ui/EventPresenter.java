package com.nairbspace.octoandroid.ui;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

/**
 * @param <T> Screen object
 * @param <U> Event object
 */
public abstract class EventPresenter<T, U> extends Presenter<T> {

    @Inject EventBus mEventBus;

    @Override
    protected void onResume() {
        super.onResume();
        mEventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEventBus.unregister(this);
    }

    /**
     * Public method needed for EventBus. Do not override this method. Do all the coding
     * on {@link #onEvent} method.
     * @param u Event object
     */
    @Subscribe
    public void subscribedEvent(U u) {
        onEvent(u);
    }

    /**
     * Do all logic for received event here. Runs on whatever thread the event was posted in.
     * @param u Event object
     */
    protected abstract void onEvent(U u);
}
