package com.nairbspace.octoandroid.model.mapper;

import com.nairbspace.octoandroid.domain.AddPrinter;
import com.nairbspace.octoandroid.domain.Printer;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.AddPrinterModel;
import com.nairbspace.octoandroid.model.PrinterModel;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DomainMapper {

    @Inject
    public DomainMapper() {
        // TODO Figure out better way to map
    }

    public Observable<AddPrinter> transformObservable(final AddPrinterModel addPrinterModel) {
        //noinspection unchecked
        return setThreads(Observable.create(new Observable.OnSubscribe<AddPrinter>() {
            @Override
            public void call(Subscriber<? super AddPrinter> subscriber) {
                try {
                    AddPrinter addPrinter = AddPrinter.builder()
                            .accountName(addPrinterModel.accountName())
                            .ipAddress(addPrinterModel.ipAddress())
                            .port(addPrinterModel.port())
                            .apiKey(addPrinterModel.apiKey())
                            .isSslChecked(addPrinterModel.isSslChecked())
                            .build();
                    subscriber.onNext(addPrinter);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException());
                }
            }
        }));
    }

    public Observable<PrinterModel> transformObservable(final Printer printer) {
        //noinspection unchecked
        return setThreads(Observable.create(new Observable.OnSubscribe<PrinterModel>() {
            @Override
            public void call(Subscriber<? super PrinterModel> subscriber) {
                try {
                    PrinterModel model = PrinterModel.builder()
                            .id(printer.id())
                            .name(printer.name())
                            .apiKey(printer.apiKey())
                            .scheme(printer.scheme())
                            .host(printer.host())
                            .port(printer.port())
                            .build();
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException());
                }
            }
        }));
    }

    public Observable setThreads(Observable observable) {
        return observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
