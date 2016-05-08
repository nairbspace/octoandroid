package com.nairbspace.octoandroid.data.cache;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;

import rx.Observable;

public interface PrinterCache {

    Observable<PrinterDbEntity> get();
}
