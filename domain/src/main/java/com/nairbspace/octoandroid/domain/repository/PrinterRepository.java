package com.nairbspace.octoandroid.domain.repository;

import com.nairbspace.octoandroid.domain.Printer;

import rx.Observable;

public interface PrinterRepository {

    Observable<Printer> printerDetails();
}
