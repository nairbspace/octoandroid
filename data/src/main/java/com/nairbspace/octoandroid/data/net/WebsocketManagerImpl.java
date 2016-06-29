package com.nairbspace.octoandroid.data.net;

import android.support.annotation.NonNull;

import com.appunite.websocket.rx.object.RxObjectWebSockets;
import com.appunite.websocket.rx.object.messages.RxObjectEventMessage;
import com.nairbspace.octoandroid.data.entity.WebsocketEntity;

import java.net.ProtocolException;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

public class WebsocketManagerImpl implements WebsocketManager {

    private final RxObjectWebSockets mRxObjectWebSockets;
    private boolean mProtocolException = false;

    @Inject
    public WebsocketManagerImpl(RxObjectWebSockets rxObjectWebSockets) {
        mRxObjectWebSockets = rxObjectWebSockets;
    }

    @Override
    public Observable<WebsocketEntity> getWebsocketObservable() {
        return mRxObjectWebSockets.webSocketObservable()
                .sample(1, TimeUnit.SECONDS)
                .compose(filterAndMap(RxObjectEventMessage.class))
                .compose(RxObjectEventMessage.filterAndMap(WebsocketEntity.class));
    }

    @NonNull
    private <T> Observable.Transformer<Object, T> filterAndMap(@NonNull final Class<T> clazz) {
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

    @Override
    public Action1<Throwable> logOnError() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable t) {
                if (t instanceof SocketException) return;
                if (t instanceof ProtocolException && !mProtocolException) {
                    mProtocolException = true;
                    Timber.e(t, null);
                }
            }
        };
    }
}
