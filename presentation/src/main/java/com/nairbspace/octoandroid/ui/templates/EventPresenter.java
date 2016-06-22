package com.nairbspace.octoandroid.ui.templates;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @param <T> Screen object
 * @param <U> Event object
 */
public abstract class EventPresenter<T, U> extends Presenter<T> {

    private final EventBus mEventBus;

    public EventPresenter(EventBus eventBus) {
        mEventBus = eventBus;
    }

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
     * @param o Event object
     */
    @SuppressWarnings("unchecked")
    @Subscribe
    public void subscribedEvent(Object o) {
        try {
            onEvent((U) o);
        } catch (ClassCastException e) {
            // Do nothing
        }
    }

    /**
     * Do all logic for received event here. Runs on whatever thread the event was posted in.
     * @param u Event object
     */
    protected abstract void onEvent(U u);
}
