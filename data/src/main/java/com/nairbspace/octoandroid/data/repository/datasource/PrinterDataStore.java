package com.nairbspace.octoandroid.data.repository.datasource;

import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import rx.Observable;

public interface PrinterDataStore {

    Observable<VersionEntity> printerVersionEntity();

    Observable<ConnectionEntity> connectionEntityDetails();
}
