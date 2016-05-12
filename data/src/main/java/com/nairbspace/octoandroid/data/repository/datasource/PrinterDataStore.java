package com.nairbspace.octoandroid.data.repository.datasource;

import com.nairbspace.octoandroid.data.entity.ConnectionEntity;

import rx.Observable;

public interface PrinterDataStore {

    Observable<ConnectionEntity> connectionDetails();

}
