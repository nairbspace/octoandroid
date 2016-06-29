package com.nairbspace.octoandroid.data.net;

import com.nairbspace.octoandroid.data.entity.WebsocketEntity;

import rx.Observable;
import rx.functions.Action1;

public interface WebsocketManager {

    Observable<WebsocketEntity> getWebsocketObservable();

    Action1<Throwable> logOnError();

}
