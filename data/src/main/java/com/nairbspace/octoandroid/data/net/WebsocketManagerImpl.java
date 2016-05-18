package com.nairbspace.octoandroid.data.net;

import com.appunite.websocket.rx.object.RxObjectWebSockets;
import com.appunite.websocket.rx.object.messages.RxObjectEventMessage;
import com.nairbspace.octoandroid.data.entity.WebsocketEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class WebsocketManagerImpl implements WebsocketManager {

    private final RxObjectWebSockets mRxObjectWebSockets;

    @Inject
    public WebsocketManagerImpl(RxObjectWebSockets rxObjectWebSockets) {
        mRxObjectWebSockets = rxObjectWebSockets;
    }

    @Override
    public Observable<WebsocketEntity> getWebsocketObservable() {
        return mRxObjectWebSockets.webSocketObservable()
                .compose(filterAndMap(RxObjectEventMessage.class))
                .compose(RxObjectEventMessage.filterAndMap(WebsocketEntity.class));
    }

    @Nonnull
    private <T> Observable.Transformer<Object, T> filterAndMap(@Nonnull final Class<T> clazz) {
        return new Observable.Transformer<Object, T>() {
            @Override
            public Observable<T> call(Observable<Object> observable) {
                return observable
                        .filter(new Func1<Object, Boolean>() {
                            @Override
                            public Boolean call(Object o) {
                                return o != null && clazz.isInstance(o);
                            }
                        })
                        .map(new Func1<Object, T>() {
                            @Override
                            public T call(Object o) {
                                //noinspection unchecked
                                return (T) o;
                            }
                        });
            }
        };
    }
}
