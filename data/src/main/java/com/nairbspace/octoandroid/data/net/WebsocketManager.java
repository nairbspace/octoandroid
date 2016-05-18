package com.nairbspace.octoandroid.data.net;

import com.nairbspace.octoandroid.data.entity.WebsocketEntity;

import rx.Observable;

public interface WebsocketManager {

    Observable<WebsocketEntity> getWebsocketObservable();

}
