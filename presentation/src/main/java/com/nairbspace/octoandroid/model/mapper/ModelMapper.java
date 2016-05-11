package com.nairbspace.octoandroid.model.mapper;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.domain.pojo.AddPrinter;
import com.nairbspace.octoandroid.domain.pojo.Connection;
import com.nairbspace.octoandroid.domain.pojo.Printer;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.AddPrinterModel;
import com.nairbspace.octoandroid.model.ConnectionModel;
import com.nairbspace.octoandroid.model.PrinterModel;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ModelMapper {

    private final Gson mGson;

    @Inject
    public ModelMapper(Gson gson) {
        mGson = gson;
        // TODO Figure out better way to map
    }

    public Observable<AddPrinter> transformObs(final AddPrinterModel addPrinterModel) {
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

    public Observable<PrinterModel> transformObs(final Printer printer) {
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

    public Observable<ConnectionModel> transformObs(final Connection connection) {
        //noinspection unchecked
        return setThreads(Observable.create(new Observable.OnSubscribe<ConnectionModel>() {
            @Override
            public void call(Subscriber<? super ConnectionModel> subscriber) {
                try {
                    ConnectionModel connectionModel = transform(connection, ConnectionModel.class);
                    subscriber.onNext(connectionModel);
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException());
                }
            }
        }));
    }

    /**
     *
     * @param inputObject Input object
     * @param outputType Output class
     * @param <T> Generic output
     * @return Output object
     */
    public <T> T transform(Object inputObject, Class<T> outputType) {
        String json = mGson.toJson(inputObject);
        return mGson.fromJson(json, outputType);
    }

    // TODO need to fix threads
    public Observable setThreads(Observable observable) {
        return observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
